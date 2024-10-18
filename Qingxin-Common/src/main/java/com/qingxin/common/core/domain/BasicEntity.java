package com.qingxin.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.qingxin.common.utils.SecurityUtils.getUserId;

@Data
public class BasicEntity implements Serializable {

    private static final long serialVersionUID = 3568046178869491465L;

    @TableId(value = "id", type= IdType.ASSIGN_UUID)
    private String id;


    /** 搜索值 */
    @TableField(exist = false)
    private String searchValue;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @TableField(exist = false)
    private String remark;

    /** 请求参数 */
    @TableField(exist = false)
    private Map<String, Object> params;

    public void setCreate()
    {
        this.createBy = getUserId().toString();
        this.createTime = new Date();
    }
    public void setUpdate()
    {
        this.updateBy = getUserId().toString();
        this.updateTime = new Date();
    }
}
