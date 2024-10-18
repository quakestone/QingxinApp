package com.qingxin.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qingxin.app.domain.po.CrmAppMenu;
import com.qingxin.app.mapper.CrmAppMenuMapper;
import com.qingxin.app.service.CrmAppMenuService;
import com.qingxin.common.exception.ServiceException;
import com.qingxin.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
@Service
public class CrmAppMenuServiceImpl extends ServiceImpl<CrmAppMenuMapper, CrmAppMenu> implements CrmAppMenuService {

    /**
     * 提交
     * @param crmAppMenu
     */
    @Transactional
    @Override
    public void submit(CrmAppMenu crmAppMenu) {
        if(StringUtils.isEmpty(crmAppMenu.getAppId())){
            throw new ServiceException("应用ID不能为空");
        }
        if(CollectionUtils.isEmpty(crmAppMenu.getMenuIds())){
            throw new ServiceException("请选择菜单");
        }
        this.remove(Wrappers.<CrmAppMenu>query().lambda().eq(CrmAppMenu::getAppId, crmAppMenu.getMenuId()));
        List<CrmAppMenu> addList = new ArrayList<>();
        for(Long l : crmAppMenu.getMenuIds()){
            CrmAppMenu newCrmAppMenu = new CrmAppMenu();
            newCrmAppMenu.setAppId(crmAppMenu.getAppId());
            newCrmAppMenu.setCreate();
            newCrmAppMenu.setMenuId(l);
            newCrmAppMenu.setAppId(crmAppMenu.getAppId());
            addList.add(newCrmAppMenu);
        }
        this.saveBatch(addList);
    }
}
