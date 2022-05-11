package com.xichuan.framework.web;


import com.xichuan.framework.web.helper.InitMethod;
import com.xichuan.framework.web.data.RequestHandler;
import com.xichuan.framework.web.data.Request;
import com.xichuan.framework.web.data.RequestParam;
import com.xichuan.framework.web.data.View;
import com.xichuan.framework.web.helper.HandlerAdapter;
import com.xichuan.framework.core.Container;
import com.xichuan.framework.web.helper.HandlerMapping;
import com.xichuan.framework.web.helper.ViewResolver;

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
    //包的基础路径
    private String basePackage;

    public DispatcherServlet(){}

    public DispatcherServlet(String basePackage){
        this.basePackage = basePackage;
        init(null);
    }

    /**
     * 初始化方法
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) {
        Container.classLoader = this.getClass().getClassLoader();
        InitMethod.init(basePackage);
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
        String requestPath  = req.getRequestURI();//获取到的路径类似 /aa=xxx
        Request request=new Request(requestPath,requestMethod);

        //交给处理器映射器处理
        RequestHandler requestHandler = HandlerMapping.getRequestHandler(request);
        if(requestHandler ==null)
            return;

        //封装RequestParam
        RequestParam param=new RequestParam();
        param.creatParam(req);

        //请求处理器适配器适配器适配Param
        Object result = HandlerAdapter.adapterForRequest(param, requestHandler);

        //对对返回的View进行处理
        if(result instanceof View) {
            ViewResolver.handleViewResult((View) result,req,resp);
        }else if(result instanceof String) {
            ViewResolver.handleDataResult((String) result,resp);
        }



    }
}
