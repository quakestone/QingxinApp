package com.qingxin.app.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qingxin.common.annotation.Excel;
import com.qingxin.common.core.domain.BasicEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("crm_app_price")
public class CrmAppPrice extends BasicEntity {

    private static final long serialVersionUID = 1L;

    /**  */
    @Excel(name = "")
    private String appId;

    /**  */
    @Excel(name = "")
    private String priceName;

    /**  */
    @Excel(name = "")
    private BigDecimal price;

    /**  */
    @Excel(name = "")
    private Long appTimestamp;
}
