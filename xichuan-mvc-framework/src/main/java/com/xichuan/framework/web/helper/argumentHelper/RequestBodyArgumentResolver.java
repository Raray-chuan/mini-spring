package com.xichuan.framework.web.helper.argumentHelper;

/**
 * Created by XiChuan on 2022/5/13.
 */

import com.alibaba.fastjson.JSON;
import com.xichuan.framework.web.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 含有@RequestBody注解，映射成一个对象
 */
public class RequestBodyArgumentResolver implements ArgumentResolver {
    @Override
    public boolean support(Class<?> type, int paramIndex, Method method) {
        Annotation[][] an = method.getParameterAnnotations();

        Annotation[] paramAns = an[paramIndex];

        for (Annotation paramAn : paramAns) {
            if (RequestBody.class.isAssignableFrom(paramAn.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) throws IOException {
        final Parameter[] parameters = method.getParameters();
        //读取流对象，需要根据req getInputStream 得到一个流对象，从这个流对象中去获取
        InputStream inputStream=request.getInputStream();
        //利用 contentLength 拿到请求中的body的字节数
        int length=request.getContentLength();
        byte[] bytes=new byte[length];
        inputStream.read(bytes);
        return JSON.parseObject(new String(bytes,"utf-8"),parameters[paramIndex].getType());
    }


}
