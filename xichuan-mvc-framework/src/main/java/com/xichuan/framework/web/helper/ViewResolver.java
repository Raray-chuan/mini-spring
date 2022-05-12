package com.xichuan.framework.web.helper;

import com.alibaba.fastjson.JSON;
import com.xichuan.framework.ConfigConstant;
import com.xichuan.framework.core.helper.ConfigHelper;
import com.xichuan.framework.web.data.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * View处理类
 */
public class ViewResolver {
    private static Logger logger = LoggerFactory.getLogger(ViewResolver.class);

    //用户自定义404页面是否存在
    private static boolean html404IsExists = false;
    //是否搜寻过用户自定义的404页面
    private static boolean hadFind404Html = false;

    /**
     * 判断是否存在用户自定义的404页面
     * @return
     * @throws IOException
     */
    private static boolean is404HtmlExist() throws IOException{
        if (!hadFind404Html){
            //判断是否存在自定义的404页面
            URL url = Thread.currentThread().getContextClassLoader().getResource("templates");

            if (url != null){
                if (url.getProtocol().equals("file")){
                    for (File childFile : new File(url.getPath()).listFiles()) {
                        if (!childFile.isDirectory() && childFile.getName().equals("404.html")) {
                            html404IsExists = true;
                            break;
                        }
                    }
                } else if (url.getProtocol().equals("jar")) {
                    String[] jarInfo = url.getPath().split("!");
                    String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
                    Enumeration<JarEntry> entrys = new JarFile(jarFilePath).entries();
                    JarEntry jarEntry;
                    while (entrys.hasMoreElements()) {
                        jarEntry = entrys.nextElement();
                        if (!jarEntry.isDirectory() && jarEntry.getName().equals("templates/404.html")){
                            html404IsExists = true;
                            break;
                        }
                    }
                }
            }
            hadFind404Html = true;
        }
        return html404IsExists;
    }

    /**
     * 处理404
     * @param response
     */
    public static void handle404(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        //获取404页面是否存在
        is404HtmlExist();

        //如果存在自定义404页面跳转到404页面，否则自动生成一个
        if (html404IsExists){
            request.getRequestDispatcher("/404.html").forward(request, response);
        }else{
            final ServletOutputStream out = response.getOutputStream();
            response.setContentType("text/html");
            response.setStatus(404);
            String html404 =
                    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "    <head>\n" +
                            "        <meta charset=\"UTF-8\">\n" +
                            "        <title>404</title>\n" +
                            "    </head>\n" +
                            "    <body>\n" +
                            "        <p>\n" +
                            "            <h1>Whitelabel Error Page</h1> \n  " +
                            "This application has no explicit mapping for /error, so you are seeing this as a fallback.</br>\n" +
                            "</br>\n" +
                            new Date().toString() + "</br>\n" +
                            "There was an unexpected error (type=Not Found, status=404).</br>\n" +
                            "No message available</br>\n" +
                            "        </p>\n" +
                            "    </body>\n" +
                            "</html>";
            out.write(html404.getBytes());
        }


    }

    /**
     * 如果Controller返回的是View，直接Response返回数据
     * @param view
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public static void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if(view.getModel().size()>0)
        {
            handleDataResult(view, response);
        }else {
            if (path != null) {
                if (path.startsWith("/")) { //重定向
                    response.sendRedirect(request.getContextPath() + path);
                } else { //请求转发
                    Map<String, Object> model = view.getModel();
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                    String configPath = ConfigHelper.getString(ConfigConstant.SERVER_BASE_PATH);
                    if (configPath == null)
                        request.getRequestDispatcher(configPath + path).forward(request, response);
                    else
                        request.getRequestDispatcher("/WEB-INF/view/" + path).forward(request, response);
                }
            }
        }
    }

    /**
     * 将view需要转发的参数写入到HttpServletResponse中
     * @param data
     * @param response
     * @throws IOException
     */
    public static void handleDataResult(View data, HttpServletResponse response) throws IOException {
        //View转发的参数
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JSON.toJSON(model).toString();
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }

    /**
     * 如果Controller返回的是数据，直接Response返回数据
     * @param data
     * @param response
     * @throws IOException
     */
    public static void handleDataResult(String data, HttpServletResponse response) throws IOException {
        if (data != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(data);
            writer.flush();
            writer.close();
        }
    }
}
