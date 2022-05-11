package com.xichuan.framework.web;


import com.xichuan.framework.core.SpringContext;
import com.xichuan.framework.web.helper.HandlerMapping;
import com.xichuan.framework.web.helper.TomcatServer;

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
        TomcatServer tomcatServer = new TomcatServer();
        //加载bean
        SpringContext context = new SpringContext(cls,null);
        //对Controller类的一些处理
        HandlerMapping.getAllHandler();

        try {
            //执行tomcatServer处理类
            tomcatServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
