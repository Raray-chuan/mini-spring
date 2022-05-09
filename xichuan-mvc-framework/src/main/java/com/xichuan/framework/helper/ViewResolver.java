package com.xichuan.framework.helper;

import com.alibaba.fastjson.JSON;
import com.xichuan.framework.data.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * View处理类
 */
public class ViewResolver {

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
                    ResourceBundle bundle = ResourceBundle.getBundle("config");
                    String configPath = bundle.getString("webPath");
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
