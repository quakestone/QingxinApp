package com.qingxin.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingxin.app.domain.dto.CrmAppPrice;
import com.qingxin.app.domain.vo.AppPriceVO;
import com.qingxin.app.mapper.CrmAppPriceMapper;
import com.qingxin.app.service.CrmAppPriceService;
import com.qingxin.common.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CrmAppPriceServiceImpl extends ServiceImpl<CrmAppPriceMapper, CrmAppPrice> implements CrmAppPriceService {

    @Resource
    private CrmAppPriceMapper crmAppPriceMapper;

    @Override
    public List<AppPriceVO> list(AppPriceVO appPriceVO) {

        return crmAppPriceMapper.priceList(appPriceVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CrmAppPrice crmAppPrice) {
        if(!StringUtils.isNotEmpty(crmAppPrice.getPriceName())){
            throw new ServiceException("套餐名称不能为空");
        }
        long count = this.count(Wrappers.<CrmAppPrice>query().lambda().eq(CrmAppPrice::getPriceName, crmAppPrice.getPriceName()));
        if(count>0){
            throw new ServiceException("套餐名称重复");
        }
        crmAppPrice.setCreate();
        this.save(crmAppPrice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(CrmAppPrice crmAppPrice) {
        if(StringUtils.isEmpty(crmAppPrice.getId())){
            throw new ServiceException("id不能为空");
        }
        if(StringUtils.isNotEmpty(crmAppPrice.getCreateBy())){
            throw  new ServiceException("不可修改创建者");
        }
        if(crmAppPrice.getCreateTime()!=null){
            throw  new ServiceException("不可修改创建时间");
        }
        crmAppPrice.setUpdate();
        this.updateById(crmAppPrice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(List<String> ids) {
        this.removeBatchByIds(ids);
    }
}
