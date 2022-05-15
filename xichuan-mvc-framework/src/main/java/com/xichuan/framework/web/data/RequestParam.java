package com.xichuan.framework.web.data;

import com.xichuan.framework.web.helper.argumentHelper.ArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * 每一个Http请求的参数，封装成一个类
 */
public class RequestParam {
    //将请求的参数封装成为map
    private Object[] params;

    /**
     * 将HttpServletRequest中的请求参数放置到Object[]
     * @param request
     */
    public void creatParam(List<ArgumentResolver> argumentResolvers,RequestHandler requestHandler,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Class<?>[] paramClazzs = requestHandler.getControllerMethod().getParameterTypes();

        params = new Object[paramClazzs.length];
        int paramIndex = 0;
        int i = 0;
        for (Class<?> paramClazz : paramClazzs) {
            for (ArgumentResolver resolver : argumentResolvers) {
                ArgumentResolver ar = (ArgumentResolver)resolver;

                if (ar.support(paramClazz, paramIndex, requestHandler.getControllerMethod())) {
                    params[i++] = ar.argumentResolver(request,
                            response,
                            paramClazz,
                            paramIndex,
                            requestHandler.getControllerMethod());
                }
            }
            paramIndex++;
        }

    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
