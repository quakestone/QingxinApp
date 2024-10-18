package com.qingxin.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingxin.app.domain.dto.CrmAppPrice;
import com.qingxin.app.domain.vo.AppPriceVO;

import java.util.List;

public interface CrmAppPriceService extends IService<CrmAppPrice> {

    /**
     * 获取应用套餐列表
     * @param appPriceVO 应用套餐vo对象
     * @return 应用套餐列表
     */
    List<AppPriceVO> list(AppPriceVO appPriceVO);


    /**
     * 添加
     * @param crmAppPrice
     */
    void add(CrmAppPrice crmAppPrice);

    /**
     * 修改
     * @param crmAppPrice
     */
    void edit(CrmAppPrice crmAppPrice);

    /**
     * 删除
     * @param ids
     */
    void del(List<String> ids);
}
