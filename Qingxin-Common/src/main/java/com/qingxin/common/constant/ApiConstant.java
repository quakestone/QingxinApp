package com.qingxin.common.constant;

/**
 *  api常量
 */
public class ApiConstant {
    //当前版本
    public static final double VERSION_ONE = 1.0;

    /**
     * 应用名前缀：SAAS
     */
    public static final String APPLICATION_NAME_PREFIX_SAAS = "qingxin-";

    /**
     * 系统服务名
     */
    public static class Server {

        /**
         * 模块服务名称：公共服务
         */
        public static final String COMMON_SERVER_NAME = APPLICATION_NAME_PREFIX_SAAS + "manage";
    }

    /**
     * 项目常量
     */
    public static class Project {

        /**
         * 应用模块
         */
        public static final String SAAS_MANAGE = "/saas/app";
        /**
         * 租户模块
         */
        public static final String SAAS_TENANT = "/saas/tenant";


        public static final String SAAS_FLOWABLE = "/saas/flowable";


    }

    /**
     * 登入类型
     */
    public static class LoginType {

        /**
         * 登入类型：微信管理
         */
        public static final String VX_ADMIN = "vx_admin";
    }
}
