package com.xichuan.framework.proxyUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * MVC代理类
 */
public class MvcProxy {
    /**
     * 代理调用调用Controller目标方法，并返回
     * @param target 目标Controller类
     * @param method 目标类的方法
     * @param paramMap 请求参数
     * @return
     */
    public static Object invokeMethod(Object target, Method method, Map<String,Object> paramMap) {
            Object res = null;

        try {
            method.setAccessible(true);
            if(paramMap!=null) {
                Parameter[] parameters = method.getParameters();
                Object[] par = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    par[i] = paramMap.get(parameters[i].getName());
                    //String a=parameters[i].getName();
                }
                res = method.invoke(target, par);
            }else
                res = method.invoke(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return res;
    }
}
