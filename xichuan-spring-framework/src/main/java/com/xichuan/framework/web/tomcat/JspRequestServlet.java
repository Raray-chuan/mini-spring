package com.xichuan.framework.web.tomcat;

import org.apache.jasper.servlet.JspServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Xichuan
 * @Date 2022/5/12 16:17
 * @Description 此Servlet对jsp的请求进行拦截
 */
@WebServlet(name = "jsp_request",urlPatterns = "*.jps",loadOnStartup = 2)
public class JspRequestServlet extends JspServlet {
    private Logger logger = LoggerFactory.getLogger(JspRequestServlet.class);

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("jsp request,request uri:+" + request.getRequestURI());
        super.service(request, response);
    }
}
