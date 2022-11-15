package com.xichuan.framework.web.helper;


import com.xichuan.framework.web.annotation.ResponseBody;
import com.xichuan.framework.web.data.RequestHandler;
import com.xichuan.framework.web.data.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description 对View处理的适配
 */
public class ViewAdapter {
    public static void adapter(Object result, RequestHandler requestHandler, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //对对返回的View进行处理
        if (result instanceof View) {
            ViewResolver.handleViewResult((View) result, request, response);
        } else {
            //判断class或者method上是否有@ResponseBody;如果有则返回json,否则返回String
            if(requestHandler.getController().getClass().isAnnotationPresent(ResponseBody.class) || requestHandler.getControllerMethod().isAnnotationPresent(ResponseBody.class)){
                ViewResolver.handleJsonResult(result,response);
            }else{
                ViewResolver.handleStingResult(result,response);
            }
        }
    }


}
