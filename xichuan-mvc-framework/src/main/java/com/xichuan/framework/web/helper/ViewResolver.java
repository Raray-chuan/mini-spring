package com.xichuan.framework.web.helper;

import com.alibaba.fastjson.JSON;
import com.xichuan.framework.ConfigConstant;
import com.xichuan.framework.core.helper.ConfigHelper;
import com.xichuan.framework.core.helper.Utils;
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
     * 处理404
     * @param response
     */
    public static void handle404Result(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        //获取404页面是否存在
        is404HtmlExist();

        //如果存在自定义404页面跳转到404页面，否则自动生成一个
        if (html404IsExists){
            request.getRequestDispatcher("/404.html").forward(request, response);
        }else{
            final ServletOutputStream out = response.getOutputStream();
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
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
     * 如果Controller返回的是View，在此处处理
     * @param view
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public static void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //如果以"/"开头，重定向；否则请求转发
        String path = view.getPath();
        String directPath = null;
        if (!(path.endsWith(".jsp")
                || path.endsWith(".html")
                || path.endsWith(".ico")
                || path.endsWith(".js")
                || path.endsWith(".css")
                || path.endsWith(".png")
                || path.endsWith(".gif")
                || path.endsWith(".jpg")
                || path.endsWith(".ttf")
                || path.endsWith(".woff")
                || path.endsWith(".woff2"))){
            String basePath = UrlUtil.formatUrl(ConfigHelper.getString(ConfigConstant.SERVER_BASE_PATH));
            String fullPath = UrlUtil.formatUrl(basePath + path);;
            directPath = fullPath.substring(0,fullPath.length() - 1);
        }else{
            String tempPath = UrlUtil.formatUrl(path);
            directPath = tempPath.substring(0,tempPath.length()-1);
        }


        if (Utils.isNotBlack(path)){
            if (path.startsWith("/")) { //重定向
                response.setCharacterEncoding("UTF-8");
                response.sendRedirect( directPath);
            } else { //请求转发
                Map<String, Object> model = view.getModel();
                String urlParam = "?";
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                    urlParam += entry.getKey() + "=" + entry.getValue() + "&";
                }
                urlParam = urlParam.substring(0,urlParam.length()-1);
                if (!(path.endsWith(".jsp")
                        || path.endsWith(".html")
                        || path.endsWith(".ico")
                        || path.endsWith(".js")
                        || path.endsWith(".css")
                        || path.endsWith(".png")
                        || path.endsWith(".gif")
                        || path.endsWith(".jpg")
                        || path.endsWith(".ttf")
                        || path.endsWith(".woff")
                        || path.endsWith(".woff2"))) {
                    directPath = directPath + urlParam;
                }
                request.setCharacterEncoding("UTF-8");
                request.getRequestDispatcher(directPath).forward(request, response);
            }
        }
    }

    /**
     * 如果Controller返回的是对象(不是view),且含有@ResponseBody注解，返回json
     * @param data
     * @param response
     * @throws IOException
     */
    public static void handleJsonResult(Object data, HttpServletResponse response) throws IOException {
        if (data != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JSON.toJSON(data).toString();
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }

    /**
     * 如果Controller返回的是对象(不是view),且不含有@ResponseBody注解，返回String
     * @param data
     * @param response
     * @throws IOException
     */
    public static void handleStingResult(Object data, HttpServletResponse response) throws IOException {
        // text/html ： HTML格式
        // text/plain ：纯文本格式
        if (data != null) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JSON.toJSON(data).toString();
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }




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
}
