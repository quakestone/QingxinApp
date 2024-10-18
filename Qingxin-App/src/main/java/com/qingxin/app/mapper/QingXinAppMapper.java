package com.qingxin.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingxin.app.domain.dto.SysApp;

import java.util.List;

public interface QingXinAppMapper extends BaseMapper<SysApp> {
    /**
     * 分页
     *
     * @param sysApp 【应用对象】
     * @return 【应用对象】集合
     */
    List<SysApp> selectSysAppList(SysApp sysApp);
}
