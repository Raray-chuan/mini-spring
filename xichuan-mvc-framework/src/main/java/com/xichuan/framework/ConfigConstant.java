package com.xichuan.framework;

/**
 * 提供相关配置项常量
 */
public interface ConfigConstant {

    //配置文件的名称
    String CONFIG_FILE = "config.properties";

    //扫描根路径
    String BASE_PACKAGE = "scan.dir";
    //
    String SERVER_BASE_PATH = "server.base.path";

    String SERVER_PORT = "server.port";

    //数据库
    String JDBC_DRIVER = "xichuan.framework.jdbc.driver";
    String JDBC_URL = "xichuan.framework.jdbc.url";
    String JDBC_USERNAME = "xichuan.framework.jdbc.username";
    String JDBC_PASSWORD = "xichuan.framework.jdbc.password";

    //文件地址
    String JSP_PATH = "xichuan.framework.app.templates.path";
    String ASSET_PATH = "xichuan.framework.app.static.path";
}
