package com.xichuan.framework.web.tomcat;

import com.xichuan.framework.ConfigConstant;
import com.xichuan.framework.core.helper.ConfigHelper;
import com.xichuan.framework.core.helper.Utils;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:18
 * @Description 集成Tomcat服务器，将请求转发至DispatcherServlet
 */
public class TomcatServer {

    //tomcat
    private Tomcat tomcat;

    /**
     * 执行tomcat初始化操作
     * @throws LifecycleException
     */
    public void start() throws LifecycleException, ServletException, IOException {
        String tomcatBaseDir = createTempDir("tomcat", ConfigHelper.getInt(ConfigConstant.SERVER_PORT)).getAbsolutePath();
        String contextDocBase = createTempDir("tomcat-docBase", ConfigHelper.getInt(ConfigConstant.SERVER_PORT)).getAbsolutePath();
        String hostName = Utils.getHostName();
        String contextPath = "";

        //实例化tomcat
        tomcat = new Tomcat();
        tomcat.setBaseDir(tomcatBaseDir);
        tomcat.setPort(ConfigHelper.getInt(ConfigConstant.SERVER_PORT));
        tomcat.setHostname(hostName);

        //添加静态资源映射
        //扫描templates/ 与 static/ 目录下的静态资源
        Host host = tomcat.getHost();
        Context context = tomcat.addWebapp(host, contextPath, contextDocBase, new EmbededContextConfig());

        //标准的web项目会一个WEB-INF/lib文件加存放额外的jar包，此处就是为了加载里面的jar（因为我们这个不是标准的web项目，此处可以不使用）
        //如果是在IED中，扫描WEB-INF/lib路径;如果是在打包好的jar，扫描WEB-INF/classes
        context.setJarScanner(new EmbededStandardJarScanner());

        //设置类加载器
        ClassLoader classLoader = TomcatServer.class.getClassLoader();
        context.setParentClassLoader(classLoader);

        //context load WEB-INF/web.xml from classpath() (此处可以不使用)
        context.addLifecycleListener(new WebXmlMountListener());

        //启动tomcat
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


    /**
     * 创建临时文件
     * @param prefix
     * @param port
     * @return
     * @throws IOException
     */
    public static File createTempDir(String prefix, int port) throws IOException {
        File tempDir = File.createTempFile(prefix + ".", "." + port);
        tempDir.delete();
        tempDir.mkdir();
        tempDir.deleteOnExit();
        return tempDir;
    }

}
