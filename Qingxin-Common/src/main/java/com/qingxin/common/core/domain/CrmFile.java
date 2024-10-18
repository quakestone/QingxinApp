package com.qingxin.common.core.domain;


import lombok.Data;

import java.util.Date;

import static com.qingxin.common.utils.SecurityUtils.getUserId;


/**
 * 文件对象
 */
@Data
public class CrmFile {

    /**
     * 文件表主键id
     */
    private String id;
    /**
     * 业务表id 0表示为空
     */
    private String busiId;
    /**
     * 业务类型 （1.视频 2.主图 3.附图 4.制作图）
     */
    private Integer busiType;
    /**
     * 附件的真实名称
     */
    private String realName;
    /**
     * 附件的临时名称
     */
    private String  tenName;
    /**
     * 路径
     */
    private String  path;
    /**
     * 文件后缀
     */
    private String  format;
    /**
     * 大小
     */
    private Integer    size;
    /**
     * 创建者
     */
    private String  createUser;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String  updateUser;
    /**
     * 更新时间
     */
    private Date    updateTime;
    public void setCreate()
    {
        this.createUser = getUserId().toString();
        this.createTime = new Date();
    }
    public void setUpdate()
    {
        this.updateUser = getUserId().toString();
        this.updateTime = new Date();
    }
    public void isBusiId(String busiId){
        if(busiId==null || busiId.equals("")){
            this.busiId="0";
        }
    }
}
