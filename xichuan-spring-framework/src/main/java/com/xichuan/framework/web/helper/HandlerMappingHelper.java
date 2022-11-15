package com.xichuan.framework.web.helper;


import com.xichuan.framework.ConfigConstant;
import com.xichuan.framework.core.BeanContainer;
import com.xichuan.framework.core.helper.ConfigHelper;
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
public class HandlerMappingHelper {

    //Request与RequestHandler的映射
    private static HashMap<Request, RequestHandler> requestHandlerMap =new HashMap<>();

    /**
     * 将含有@RequestMapping的方法，<Request, RequestHandler>并放在HandlerMap中
     */
    public static void getAllHandler() {
        //获取所有的Controller
        for(Object controllerName: BeanContainer.controllerMap.keySet()) {
            Object controllerInstance = BeanContainer.controllerMap.get(controllerName);

            //获取类上的@RequestMapping的value值
            String classUri = null;
            if (controllerInstance.getClass().isAnnotationPresent(RequestMapping.class)){
                classUri = controllerInstance.getClass().getDeclaredAnnotation(RequestMapping.class).value();
            }

            //遍历所有的Controller的方法
            String methodUri;
            String requestMethod;
            Request request;
            for(Method method:controllerInstance.getClass().getDeclaredMethods()) {
                //获取controller中所有带@RequestMapping的方法
                if(!method.isAnnotationPresent(RequestMapping.class)) {
                    return;
                }

                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                methodUri = requestMapping.value();
                requestMethod = requestMapping.method().name();
                //将适用的方法封装成请求体
                request = new Request(joinFullUri(classUri,methodUri),requestMethod);

                //将handler封装成对象
                RequestHandler requestHandler =new RequestHandler(controllerInstance,method);
                requestHandlerMap.put(request, requestHandler);
            }


        }
    }

    /**
     * 对server.base.path、类上的@RequestMapping的value、方法上的@RequestMapping的value进行拼接
     * @param classUri 类上的@RequestMapping的value
     * @param methodUri 方法上的@RequestMapping的value
     * @return
     */
    public static String joinFullUri(String classUri,String methodUri){
        String basePath = UrlUtil.formatUrl(ConfigHelper.getString(ConfigConstant.SERVER_BASE_PATH));
        String classPath = UrlUtil.formatUrl(classUri);
        String methodPath = UrlUtil.formatUrl(methodUri);
        return UrlUtil.formatUrl(basePath + classPath + methodPath);
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
