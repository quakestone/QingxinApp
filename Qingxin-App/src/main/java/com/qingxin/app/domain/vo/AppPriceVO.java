package com.qingxin.app.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppPriceVO {
    /**
     * 应用id
     */
    private String appId;
    /**
     * 套餐id
     */
    private String id;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 套餐名称
     */
    private String priceName;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 开始金额
     */
    private BigDecimal startAmount;
    /**
     * 结束金额
     */
    private BigDecimal endAmount;
    /**
     * 套餐时间
     */
    private Long appTimestamp;


}
