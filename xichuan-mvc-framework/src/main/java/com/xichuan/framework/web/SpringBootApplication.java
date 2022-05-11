package com.xichuan.framework.web;


import com.xichuan.framework.web.servier.TomcatServer;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:16
 * @Description
 */

/**
 * SpringBoot入口类
 */
public class SpringBootApplication {
    public static void run(Class<?> cls,String[] args){
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer(cls.getPackage().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
