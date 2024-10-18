package com.qingxin.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingxin.app.domain.dto.CrmAppPrice;
import com.qingxin.app.domain.dto.SysApp;
import com.qingxin.app.domain.po.CrmAppMenu;
import com.qingxin.app.mapper.QingXinAppMapper;
import com.qingxin.app.service.CrmAppMenuService;
import com.qingxin.app.service.CrmAppPriceService;
import com.qingxin.app.service.QingXinAppService;
import com.qingxin.common.core.domain.CrmFile;
import com.qingxin.common.exception.ServiceException;
import com.qingxin.app.utils.file.FileTableUtil;
import com.qingxin.common.utils.uuid.IdUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.ABICodecException;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.TransactionBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class QingXinAppServiceImpl extends ServiceImpl<QingXinAppMapper, SysApp> implements QingXinAppService {

    @Autowired
    private FileTableUtil fileTableUtil;

    @Resource
    private CrmAppMenuService crmAppMenuService;


    @Resource
    private CrmAppPriceService crmAppPriceService;


    //Webase注册
    public final String configFile = "Qingxin-App/src/main/resources/config-example.toml";

    BcosSDK sdk = BcosSDK.build(configFile);

    Client client = sdk.getClient(1);

    CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();

    AssembleTransactionProcessor transactionProcessor;

    {
        try {
            transactionProcessor = TransactionProcessorFactory
                    .createAssembleTransactionProcessor(
                            client, keyPair, "Qingxin-App/src/main/resources/abi/", ""
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取应用对应菜单以及套餐信息
     * @param id 应用id
     * @return 结果
     */
    @Override
    public SysApp selectSysAppById(String id) throws SQLException {
        SysApp sysApp = this.getById(id);
        List<CrmAppPrice> crmAppPriceList = crmAppPriceService.list(Wrappers.<CrmAppPrice>query()
                .select("price_name", "id", "price","app_timestamp")
                .lambda().eq(CrmAppPrice::getAppId, sysApp.getId())
        );
        sysApp.setCrmAppPriceList(crmAppPriceList);
        List<CrmAppMenu> appMenus= crmAppMenuService.list(Wrappers.<CrmAppMenu>query()
                .select("menu_id")
                .lambda().eq(CrmAppMenu::getAppId, id));
        List<Long> menuIds = appMenus.stream().map(CrmAppMenu::getMenuId).collect(Collectors.toList());
        sysApp.setSysMenuIds(menuIds);
        List<String> filePathsByBusiId = fileTableUtil.getFilePathsByBusiId(id);
        sysApp.setCrmFilePaths(filePathsByBusiId);
        return sysApp;
    }


    /**
     * 查询应用列表包含套件价格、包含应用对应的菜单
     *
     * @param sysApp 应用对象
     * @return 应用对象的集合
     */
    @Override
    public List<SysApp> selectSysAppList(SysApp sysApp) {
        //首先获取应用对象的集合
        List<SysApp> list = baseMapper.selectSysAppList(sysApp);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysApp sysApp) {
        if(!StringUtils.isNotEmpty(sysApp.getAppName())){
            throw new ServiceException("应用名称不能为空");
        }
        long count = this.count(Wrappers.<SysApp>query().lambda().eq(SysApp::getAppName,sysApp.getAppName()));
        if(count>0){
            throw new ServiceException("应用名称重复");
        }
        sysApp.setCreate();

        List<Object> params = new ArrayList<>();

        params.add(sysApp.getAppName());
        params.add(sysApp.getDescription());


        //TODO: 调用QuakeApp合约进行数据上链
        TransactionResponse transactionResponse = null;

        try {
            transactionResponse = transactionProcessor.
                    sendTransactionAndGetResponseByContractLoader(
                            "QKApp","0x130c6e36b16d167c8cb385a376e0f166f1609f42",
                            "publicApp", params
                    );
        } catch (ABICodecException | TransactionBaseException e) {
            e.printStackTrace();
        }

        getChainRes(transactionResponse);


        this.save(sysApp);
        SysApp dBSysApp = this.getOne(Wrappers.<SysApp>query().lambda().eq(SysApp::getAppName, sysApp.getAppName()));
        List<CrmFile> crmFiles = sysApp.getCrmFiles();
        if (CollectionUtils.isNotEmpty(crmFiles)){
            sysApp.getCrmFiles().forEach(item -> item.setBusiId(dBSysApp.getId()));
            try{
                fileTableUtil.addFiles(sysApp.getCrmFiles());
            }catch (Exception e){
                throw new ServiceException("文件插入失败");
            }
        }
        /**
         * 批量插入应用菜单关系
         */
        List<CrmAppMenu> crmAppMenuList = new ArrayList<>();
        sysApp.getSysMenuIds().forEach(item ->{
            CrmAppMenu crmAppMenu = new CrmAppMenu();
            crmAppMenu.setId(IdUtils.simpleUUID());
            crmAppMenu.setAppId(sysApp.getId());
            crmAppMenu.setMenuId(item);
            crmAppMenu.setCreate();
            crmAppMenuList.add(crmAppMenu);
        });
        crmAppMenuService.saveBatch(crmAppMenuList);
    }


    private void getChainRes(TransactionResponse transactionResponse) {
        List<Object> returnValues = null;
        if (transactionResponse != null) {
            returnValues = transactionResponse.getReturnObject();
        }

        if (returnValues != null){
            for (Object value : returnValues) {
                System.out.println("上链返回值: " + value.toString());
            }

        }else {
            System.out.println("上链失败,请检查!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysApp sysApp) {
        if(!StringUtils.isNotEmpty(sysApp.getId())){
            throw new ServiceException("id不能为空");
        }
        if(StringUtils.isNotEmpty(sysApp.getCreateBy())){
            throw  new ServiceException("不可修改创建者");
        }
        if(sysApp.getCreateTime()!=null){
            throw  new ServiceException("不可修改创建时间");
        }

        //在修改前先获取旧的SysApp
        SysApp oldSysApp = this.getById(sysApp.getId());
        String oldAppName =  oldSysApp.getAppName();
        List<Object> params = new ArrayList<>();
        params.add(oldAppName);



        TransactionResponse transactionResponse = null;

        try {
            transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "QKApp", "0x130c6e36b16d167c8cb385a376e0f166f1609f42",
                    "getAppIdByName", params
            );
        } catch (ABICodecException | TransactionBaseException e) {
            throw new RuntimeException(e);
        }

        BigInteger appId = null;
        List<Object> result = transactionResponse.getReturnObject();
        if (result != null){
            if(result.size() == 1){
                appId = (BigInteger) result.get(0);
            }
        }

        String appName = sysApp.getAppName();
        String appDescription = sysApp.getDescription();

        List<Object> updateParams = new ArrayList<>();
        updateParams.add(appId);
        updateParams.add(appName);
        updateParams.add(appDescription);


        // 调用合约的 updateApp 方法
        TransactionResponse transactionResponse1 = null;

        try {
            transactionResponse1 = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "QKApp",  // 合约名称
                    "0x130c6e36b16d167c8cb385a376e0f166f1609f42",  // 合约地址
                    "updateApp",  // 合约中的更新方法名
                    updateParams  // 参数列表
            );
        } catch (ABICodecException | TransactionBaseException e) {
            throw new RuntimeException(e);
        }

        // 检查交易响应是否成功
        if (transactionResponse1 != null) {
            System.out.println("更新app成功");
        } else {
            System.out.println("Failed to update app on the blockchain.");
        }

        sysApp.setUpdate();
        if(sysApp.getCrmFiles()!=null){
            try{
                fileTableUtil.updateFile(sysApp.getCrmFiles().get(0));
            }catch (Exception e){
                throw new ServiceException("文件修改失败");
            }
        }
        if(sysApp.getSysMenuIds()!=null){
            List<CrmAppMenu> crmAppMenuList = crmAppMenuService.list(Wrappers.<CrmAppMenu>query().lambda().eq(CrmAppMenu::getAppId, sysApp.getId()));
            crmAppMenuList.forEach( item ->{
                crmAppMenuService.removeById(item.getId());
            });
            sysApp.getSysMenuIds().forEach(item ->{
                CrmAppMenu newCrmAppMenu = new CrmAppMenu();
                newCrmAppMenu.setCreate();
                newCrmAppMenu.setAppId(sysApp.getId());
                newCrmAppMenu.setMenuId(item);
                crmAppMenuService.save(newCrmAppMenu);
            });
        }

        this.updateById(sysApp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(List<String> ids) {
        this.removeBatchByIds(ids);
    }
}
