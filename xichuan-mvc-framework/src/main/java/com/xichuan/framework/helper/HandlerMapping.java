package com.xichuan.framework.helper;


import com.xichuan.framework.annotation.RequestMapping;
import com.xichuan.framework.data.Handler;
import com.xichuan.framework.data.Request;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */
public class HandlerMapping {
    private static HashMap<Request, Handler> handlerMap =new HashMap<>();

    /**
     * 获取所有Controller的handler，并放在HandlerMap中
     */
    public static void getAllHandler() {
        //获取所有的Controller
        for(Object controllerName: Container.controllerMap.keySet()) {
            Object controllerInstance = Container.controllerMap.get(controllerName);
            for(Method method:controllerInstance.getClass().getDeclaredMethods()) {
                //获取controller中所有带@RequestMapping的方法
                if(!method.isAnnotationPresent(RequestMapping.class))
                    return;
                RequestMapping requestMapping=method.getAnnotation(RequestMapping.class);
                String path=requestMapping.value();
                String requestMethod=requestMapping.method().name();
                //将适用的方法封装成请求体
                Request request=new Request("/"+path,requestMethod);
                //将handler封装成对象
                Handler handler=new Handler(controllerInstance,method);

                handlerMap.put(request,handler);
            }


        }

    }

    /**
     * 通过request(uri与请求类型)获取对应的Handler
     * @param request
     * @return
     */
    public static Handler getHandler(Request request) {
        if(handlerMap.containsKey(request))
            return handlerMap.get(request);
        else
          return null;
    }
}
