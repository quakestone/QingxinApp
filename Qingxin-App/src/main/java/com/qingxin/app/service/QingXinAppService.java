package com.qingxin.app.service;

import com.qingxin.app.domain.dto.SysApp;

import java.sql.SQLException;
import java.util.List;

/**
 *   氢信应用模块接口
 */
public interface QingXinAppService {

    /**
     * 查询应用对象
     *
     * @param id 应用主键
     * @return 应用对象
     */
     SysApp selectSysAppById(String id) throws SQLException;

    /**
     * 查询应用集合包含了价格和到期时间
     *
     * @param sysApp 应用对象
     * @return 应用集合
     */
     List<SysApp> selectSysAppList(SysApp sysApp);


    /**
     * 添加
     * @param sysApp
     */
    void add(SysApp sysApp);

    /**
     * 修改
     * @param sysApp
     */
    void edit(SysApp sysApp);

    /**
     * 删除
     * @param ids
     */
    void del(List<String> ids);
}
