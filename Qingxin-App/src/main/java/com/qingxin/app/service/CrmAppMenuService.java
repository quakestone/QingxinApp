package com.qingxin.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingxin.app.domain.po.CrmAppMenu;

public interface CrmAppMenuService extends IService<CrmAppMenu> {

    /**
     * 提交
     * @param crmAppMenu
     */
    void submit(CrmAppMenu crmAppMenu);
}
