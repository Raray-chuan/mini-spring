package com.xichuan.framework.web.tomcat;





import org.apache.catalina.servlets.DefaultServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Author Xichuan
 * @Date 2022/5/12 12:48
 * @Description
 */

/**
 * 此Servlet对静态资源的请求进行拦截
 */
@WebServlet(name = "html_request",urlPatterns = {"*.html","*.ico","*.js","*.css","*.png","*.gif","*.jpg","*.ttf","*.woff","*.woff2"},loadOnStartup = 1)
public class HtmlRequestServlet extends DefaultServlet {
    private Logger logger = LoggerFactory.getLogger(HtmlRequestServlet.class);

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("html request,request uri:" + req.getRequestURI());
        super.service(req, resp);
    }
}