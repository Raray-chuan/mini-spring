package com.xichuan.framework.web.helper.argumentHelper;

import com.alibaba.fastjson.JSON;
import com.xichuan.framework.web.annotation.RequestBody;
import com.xichuan.framework.web.annotation.RequestParam;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by XiChuan on 2022/5/15.
 */
public class DefaultArgumentResolver implements ArgumentResolver {

    @Override
    public boolean support(Class<?> type, int paramIndex, Method method) {
        //除了特殊的集中参数，都走此默认的参数处理器
        Annotation[][] an = method.getParameterAnnotations();
        Annotation[] paramAns = an[paramIndex];
        boolean isAnnotationMatch = false;
        for (Annotation paramAn : paramAns) {
            if (RequestParam.class.isAssignableFrom(paramAn.getClass()) || RequestBody.class.isAssignableFrom(paramAn.getClass())) {
                isAnnotationMatch = true;
            }
        }

        return !( ServletRequest.class.isAssignableFrom(type) || ServletResponse.class.isAssignableFrom(type) || isAnnotationMatch);
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {
        final Parameter[] parameters = method.getParameters();
        String requestStr = request.getParameter(parameters[paramIndex].getName());
        return transDataType(requestStr,parameters[paramIndex]);
    }

    /**
     * 对String进行类型转换
     * @param requestStr
     * @param parameter
     * @return
     */
    private Object transDataType(String requestStr,Parameter parameter){
        Object result = null;
        //进行类型转换
        if (String.class ==parameter.getType()) {
            result = requestStr;
        } else if (Integer.class == parameter.getType() || int.class == parameter.getType()) {
            result = Integer.valueOf(requestStr);
        } else if (Double.class == parameter.getType() || double.class == parameter.getType()) {
            result = Double.valueOf(requestStr);
        } else if(Long.class == parameter.getType() || long.class == parameter.getType()) {
            result = Long.valueOf(requestStr);
        } else if(Short.class == parameter.getType() || short.class == parameter.getType()) {
            result = Short.valueOf(requestStr);
        } else if(Boolean.class == parameter.getType() || boolean.class == parameter.getType()) {
            result = Boolean.valueOf(requestStr);
        } else if(Float.class == parameter.getType() || float.class == parameter.getType()) {
            result = Float.valueOf(requestStr);
        } else if(Byte.class == parameter.getType() || byte.class == parameter.getType()) {
            result = Byte.valueOf(requestStr);
        } else if(Map.class == parameter.getType() || HashMap.class == parameter.getType()) {
            result = JSON.parseObject(requestStr,Map.class);
        }else if (List.class == parameter.getType() || ArrayList.class == parameter.getType() || LinkedList.class == parameter.getType()){
            //todo List中的类型不一定是String,目前还没有想到好方法
            result = JSON.parseArray(requestStr,String.class);
        }
        return result;
    }
}
