package com.xichuan.framework.web.helper;

import com.xichuan.framework.ConfigConstant;
import com.xichuan.framework.core.helper.ConfigHelper;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.ServletException;
import java.io.File;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:18
 * @Description
 */

/**
 * 集成Tomcat服务器，将请求转发至DispatcherServlet
 */
public class TomcatServer {

    //tomcat
    private Tomcat tomcat;

    /**
     * 执行tomcat初始化操作
     * @throws LifecycleException
     */
    public void start() throws LifecycleException, ServletException {
        //实例化tomcat
        tomcat = new Tomcat();
        tomcat.setPort(ConfigHelper.getInt(ConfigConstant.SERVER_PORT));
        tomcat.getConnector();

        //实例化context容器
        StandardContext context = new StandardContext();
        // ”/“意思是：拦截所有的请求;如果设置像"/xichuan/",则像"/test/a"直接不拦截
        context.setPath("/");

        //listener用于监听时间的发生或状态的改变，其初始化与调用顺序在filter之前，
        //Tomcat使用两类Listener接口分别是org.apache.catalina.LifecycleListener和原生Java.util.EventListener
        context.addLifecycleListener(new Tomcat.FixContextListener());

        //将请求转发给Servlet
        Tomcat.addServlet(context,DispatcherServlet.class.getSimpleName(),new DispatcherServlet()).setAsyncSupported(true);

        //添加映射;只有是此前缀(eg:"/xichuan/*")的请求才会转发给Servlet，其他请求404
//        context.addServletMappingDecoded(UrlUtil.formatUrl(ConfigHelper.getString(ConfigConstant.SERVER_BASE_PATH)) + UrlUtil.getUrlPatternAll(),DispatcherServlet.class.getSimpleName());
        context.addServletMappingDecoded("/*",DispatcherServlet.class.getSimpleName());

        //configureResources(context);


        //启动
        tomcat.getHost().addChild(context);
        tomcat.start();

        //设置常驻线程防止tomcat中途退出
        Thread awaitThread = new Thread("tomcat-await-thread"){
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        //设置为非守护线程
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    private static void configureResources(StandardContext context) {
        String WORK_HOME = System.getProperty("user.dir");
        File classesDir = new File(WORK_HOME, "target/classes");
        File jarDir = new File(WORK_HOME, "lib");
        WebResourceRoot resources = new StandardRoot(context);
        if (classesDir.exists()) {
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
            System.out.println("Resources added: [classes]");
        } else if (jarDir.exists()) {
            resources.addJarResources(new DirResourceSet(resources, "/WEB-INF/lib", jarDir.getAbsolutePath(), "/"));
            System.out.println("Resources added: [jar]");
        } else {
            resources.addPreResources(new EmptyResourceSet(resources));
            System.out.println("Resources added: [empty]");
        }
        context.setResources(resources);
    }
}
