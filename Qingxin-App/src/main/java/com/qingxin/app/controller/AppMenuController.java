package com.qingxin.app.controller;


import com.qingxin.app.domain.po.CrmAppMenu;
import com.qingxin.app.service.CrmAppMenuService;
import com.qingxin.common.constant.ApiConstant;
import com.qingxin.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(ApiConstant.Project.SAAS_MANAGE + "/app/menu")
public class AppMenuController {
    @Resource
    private CrmAppMenuService appMenuService;

    /**
     * 提交
     */
    @PostMapping("/submit")
    public AjaxResult add(@RequestBody CrmAppMenu crmAppMenu) {
        appMenuService.submit(crmAppMenu);
        return AjaxResult.success();
    }


}
