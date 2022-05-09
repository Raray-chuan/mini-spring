package com.xichuan.framework;


import com.xichuan.framework.config.InitMethod;
import com.xichuan.framework.data.Handler;
import com.xichuan.framework.data.Request;
import com.xichuan.framework.data.RequestParam;
import com.xichuan.framework.data.View;
import com.xichuan.framework.helper.Adapter;
import com.xichuan.framework.helper.Container;
import com.xichuan.framework.helper.HandlerMapping;
import com.xichuan.framework.helper.ViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * 每一个http请求进行拦截一次
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    /**
     * 初始化方法
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        Container.classLoader=this.getClass().getClassLoader();
        InitMethod.init();
    }

    /**
     * 请求处理逻辑
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod(); //请求类型，GET/POST...
        String requestPath  = req.getPathInfo();//获取到的路径类似 /aa=xxx
        Request request=new Request(requestPath,requestMethod);

        //交给处理器映射器处理
        Handler handler= HandlerMapping.getHandler(request);
        if(handler==null)
            return;

        //封装RequestParam
        RequestParam param=new RequestParam();
        param.creatParam(req);

        //请求处理器适配器适配器适配Param
        Object result = Adapter.adapterForRequest(param,handler);

        //对对返回的View进行处理
        if(result instanceof View) {
            ViewResolver.handleViewResult((View) result,req,resp);
        }else if(result instanceof String) {
            ViewResolver.handleDataResult((String) result,resp);
        }



    }
}
