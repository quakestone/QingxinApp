package com.qingxin.app.controller;


import com.qingxin.app.domain.dto.SysApp;
import com.qingxin.app.service.QingXinAppService;
import com.qingxin.common.constant.ApiConstant;
import com.qingxin.common.core.controller.BaseController;
import com.qingxin.common.core.domain.AjaxResult;
import com.qingxin.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;



/**
 *   应用模块
 * @author  嘉伦
 */

@RestController
@RequestMapping(ApiConstant.Project.SAAS_MANAGE + "/system/app")
public class SysAppController extends BaseController {

    @Resource
    private QingXinAppService qingXinAppService;


    /**
     * 查询应用列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysApp sysApp) {
        startPage();
        List<SysApp> list = qingXinAppService.selectSysAppList(sysApp);
        return getDataTable(list);
    }

    /**
     * 修改应用
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody SysApp sysApp) {
        qingXinAppService.edit(sysApp);
        return AjaxResult.success();
    }

    /**
     *
     * 新增应用
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysApp sysApp) {
        qingXinAppService.add(sysApp);
        return AjaxResult.success();
    }

    /**
     *
     * 删除应用
     */
    @PostMapping("/del")
    public AjaxResult edit(@RequestBody List<String> ids) {
        qingXinAppService.del(ids);
        return AjaxResult.success();
    }

    /**
     * 查询单个应用对应菜单以及套餐信息
     * @param id 应用id
     * @return 结果
     */
    @GetMapping("/one")
    public AjaxResult one(String id) throws SQLException {
        return AjaxResult.success(qingXinAppService.selectSysAppById(id));
    }


}
