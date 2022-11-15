package com.xichuan.framework.web.data;

import java.lang.reflect.Method;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description Controller中一个方法封装一个方法; 此类作用是通过uri找到对应的RequestHandler
 */
public class RequestHandler {

    //Controller类
    private Object controller;

    //该请求对应Controller的方法
    private Method controllerMethod;

    public RequestHandler(Object controller, Method controllerMethod) {
        this.controller = controller;
        this.controllerMethod = controllerMethod;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }

    public void setControllerMethod(Method controllerMethod) {
        this.controllerMethod = controllerMethod;
    }
}
