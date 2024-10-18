package com.qingxin.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingxin.app.domain.dto.CrmAppPrice;
import com.qingxin.app.domain.vo.AppPriceVO;

import java.util.List;

public interface CrmAppPriceMapper extends BaseMapper<CrmAppPrice> {

    /**
     * 查询应用套餐列表
     * @param appPriceVO 应用套餐vo对象
     * @return 应用套餐列表
     */
    List<AppPriceVO>  priceList(AppPriceVO appPriceVO);
}
