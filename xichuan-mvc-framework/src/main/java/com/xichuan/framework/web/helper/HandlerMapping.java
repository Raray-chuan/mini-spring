package com.xichuan.framework.web.helper;


import com.xichuan.framework.core.Container;
import com.xichuan.framework.web.annotation.RequestMapping;
import com.xichuan.framework.web.data.RequestHandler;
import com.xichuan.framework.web.data.Request;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */
public class HandlerMapping {

    //Request与RequestHandler的映射
    private static HashMap<Request, RequestHandler> requestHandlerMap =new HashMap<>();

    /**
     * 将含有@RequestMapping的方法，<Request, RequestHandler>并放在HandlerMap中
     */
    public static void getAllHandler() {
        //获取所有的Controller
        for(Object controllerName: Container.controllerMap.keySet()) {
            Object controllerInstance = Container.controllerMap.get(controllerName);

            //遍历所有的Controller的方法
            for(Method method:controllerInstance.getClass().getDeclaredMethods()) {
                //获取controller中所有带@RequestMapping的方法
                if(!method.isAnnotationPresent(RequestMapping.class)) {
                    return;
                }

                RequestMapping requestMapping=method.getAnnotation(RequestMapping.class);
                String path = requestMapping.value();
                String requestMethod = requestMapping.method().name();
                //将适用的方法封装成请求体
                Request request=new Request("/"+path,requestMethod);

                //将handler封装成对象
                RequestHandler requestHandler =new RequestHandler(controllerInstance,method);
                requestHandlerMap.put(request, requestHandler);
            }


        }

    }

    /**
     * 通过request(uri与请求类型)获取对应的Handler
     * @param request
     * @return
     */
    public static RequestHandler getRequestHandler(Request request) {
        if(requestHandlerMap.containsKey(request))
            return requestHandlerMap.get(request);
        else
          return null;
    }
}
