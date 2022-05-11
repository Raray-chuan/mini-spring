package com.xichuan.framework.web.servier;

import com.xichuan.framework.web.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:18
 * @Description
 */

/**
 * 集成Tomcat服务器，将请求转发至DispatcherServlet
 */
public class TomcatServer {

    private Tomcat tomcat;
    private String[] agrs;

    public TomcatServer(String[] agrs) {
        this.agrs = agrs;
    }

    public void startServer(String basePackage) throws LifecycleException {
        //实例化tomcat
        tomcat = new Tomcat();
        tomcat.setPort(8080);
        //需要设置一下端口，不然无法访问
        tomcat.getConnector().setPort(8080);

        //实例化context容器
        Context context = new StandardContext();

        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        DispatcherServlet servlet = new DispatcherServlet(basePackage);
        Tomcat.addServlet(context,"dispatcherServlet",servlet).setAsyncSupported(true);
        //添加映射
        context.addServletMappingDecoded("/","dispatcherServlet");
        tomcat.getHost().addChild(context);
        tomcat.start();
        //设置常驻线程防止tomcat中途退出
        Thread awaitThread = new Thread("tomcat_await_thread."){
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        //设置为非守护线程
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
