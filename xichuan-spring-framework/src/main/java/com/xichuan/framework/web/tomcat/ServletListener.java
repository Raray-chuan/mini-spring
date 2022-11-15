package com.xichuan.framework.web.tomcat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Author Xichuan
 * @Date 2022/5/12 12:32
 * @Description
 */

/**
 * Servlet的Filter  （此处是测试代码，不将此监听器添加到tomcat）
 * 监听 ServletContext 对象的生命周期，实际上就是监听 Web 应用的生命周期
 */
//@WebListener
public class ServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ServletListener listen ServletContext int");
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletListener listen ServletContext destroy");
    }
}
