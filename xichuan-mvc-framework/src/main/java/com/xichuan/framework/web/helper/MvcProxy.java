package com.xichuan.framework.web.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * MVC请求代理类
 */
public class MvcProxy {
    /**
     * 代理调用调用Controller目标方法，并返回
     * @param target 目标Controller类
     * @param method 目标类的方法
     * @param params 请求参数
     * @return
     */
    public static Object invokeMethod(Object target, Method method, Object[] params) {
            Object res = null;

        try {
            method.setAccessible(true);
            if(params!=null && params.length > 0) {
                res = method.invoke(target, params);
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
