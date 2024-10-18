package com.qingxin.app.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.qingxin.common.annotation.Excel;
import com.qingxin.common.core.domain.BasicEntity;
import com.qingxin.common.core.domain.CrmFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysApp extends BasicEntity {
    private static final long serialVersionUID = 1L;

    /** 主图 */
    @Excel(name = "主图")
    private String logo;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String appName;

    /** 简介 */
    @Excel(name = "简介")
    private String description;

    /** 演示地址 */
    @Excel(name = "演示地址")
    private String address;

    /** 应用状态 0启用1禁用2下架 */
    @Excel(name = "应用状态 0启用1禁用2下架")
    private Integer appStatus;
    /** 应用域名*/
    @Excel(name = "应用域名")
    private String appUrl;

    /**
     * 应用套餐列表
     */
    @TableField(exist = false)
    private List<CrmAppPrice> crmAppPriceList;
    /**
     * 文件集合
     */
    @TableField(exist = false)
    private List<CrmFile> crmFiles;

    /**
     * 文件路径集合
     */
    @TableField(exist = false)
    private List<String> crmFilePaths;
    /**
     *
     * 应用对应菜单集合
     */
    @TableField(exist = false)
    public List<Long> sysMenuIds;
}
