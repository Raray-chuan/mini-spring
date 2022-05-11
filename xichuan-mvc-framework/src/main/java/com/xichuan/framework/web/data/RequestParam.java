package com.xichuan.framework.web.data;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;

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
    private HashMap<String,Object> paramMap = new HashMap<>();

    /**
     * 将HttpServletRequest中的请求参数放置到paramMap
     * @param request
     */
    public void creatParam(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        if(!parameterNames.hasMoreElements())
            return;
        while (parameterNames.hasMoreElements()) {
            String key=parameterNames.nextElement();
            String value=request.getParameter(key);
            paramMap.put(key,value);
        }

    }
    public HashMap<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(HashMap<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
