package com.xichuan.framework.web.tomcat;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
/**
 * @Author Xichuan
 * @Date 2022/5/12 11:21
 * @Description
 */

/**
 * Servlet的Filter  （此处是测试代码，不将此过滤器添加到tomcat）
 *
 *  Filter，过滤器，是处于客户端与服务器资源文件之间的一道过滤网，在访问资源文件之前，
 *  通过一系列的过滤器对请求进行修改、判断等，把不符合规则的请求在中途拦截或修改。也可以对响应进行过滤，拦截或修改响应。
 */
//@WebFilter(urlPatterns = ("/*"))
public class ServletFiler implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Servlet filter is init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        System.out.println("Servlet filter,request uri:"+ requestURI);
        chain.doFilter(request, response);

    }
    @Override
    public void destroy() {
        System.out.println("Servlet filter is destroy");
    }
}