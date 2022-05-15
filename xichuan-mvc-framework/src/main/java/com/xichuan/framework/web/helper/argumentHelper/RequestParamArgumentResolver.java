package com.xichuan.framework.web.helper.argumentHelper;



import com.alibaba.fastjson.JSON;
import com.xichuan.framework.web.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * @Author Xichuan
 * @Date 2022/5/13 20:25
 * @Description
 */
public class RequestParamArgumentResolver implements ArgumentResolver {
    
    public boolean support(Class<?> type, int paramIndex, Method method) {
        
        Annotation[][] an = method.getParameterAnnotations();
        Annotation[] paramAns = an[paramIndex];
        for (Annotation paramAn : paramAns) {
            if (RequestParam.class.isAssignableFrom(paramAn.getClass())) {
                return true;
            }
        }
        return false;
    }
    
    public Object argumentResolver(HttpServletRequest request,
                                   HttpServletResponse response, Class<?> type, int paramIndex,
                                   Method method) {
        
        Annotation[][] an = method.getParameterAnnotations();
        Annotation[] paramAns = an[paramIndex];
        
        for (Annotation paramAn : paramAns) {
            if (RequestParam.class.isAssignableFrom(paramAn.getClass())) {
                RequestParam rp = (RequestParam)paramAn;

                final Parameter[] parameters = method.getParameters();
                String requestStr = request.getParameter(rp.value());
                return transDataType(requestStr,parameters[paramIndex]);
            }
        }
        
        return null;
    }


    /**
     * 对String进行类型转换
     * @param requestStr
     * @param parameter
     * @return
     */
    private Object transDataType(String requestStr, Parameter parameter){
        Object result = null;
        //进行类型转换
        if (String.class ==parameter.getType()) {
            result = requestStr;
        } else if (Integer.class == parameter.getType()) {
            result = Integer.valueOf(requestStr);
        } else if (Double.class == parameter.getType()) {
            result = Double.valueOf(requestStr);
        } else if(Long.class == parameter.getType()) {
            result = Long.valueOf(requestStr);
        } else if(Short.class == parameter.getType()) {
            result = Short.valueOf(requestStr);
        } else if(Boolean.class == parameter.getType()) {
            result = Boolean.valueOf(requestStr);
        } else if(Float.class == parameter.getType()) {
            result = Float.valueOf(requestStr);
        } else if(Byte.class == parameter.getType()) {
            result = Byte.valueOf(requestStr);
        } else if(Map.class == parameter.getType()) {
            result = JSON.parseObject(requestStr,Map.class);
        }else if (List.class == parameter.getType()){
            result = JSON.parseArray(requestStr,parameter.getType());
        }
        return result;
    }
    
}
