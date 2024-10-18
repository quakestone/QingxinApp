package com.qingxin.app.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qingxin.common.annotation.Excel;
import com.qingxin.common.core.domain.BasicEntity;
import lombok.Data;

import java.util.List;


@Data
@TableName("crm_app_menu")
public class CrmAppMenu extends BasicEntity {

    /**
     * 应用ID
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String appId;


    /**
     * 菜单ID
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long menuId;

    /**
     * 菜单ID集合
     */
    @TableField(exist = false)
    private List<Long> menuIds;

}
