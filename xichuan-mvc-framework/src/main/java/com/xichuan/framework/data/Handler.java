package com.xichuan.framework.data;

import java.lang.reflect.Method;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * Handler处理类
 */
public class Handler {
    //Controller类
    private Object controller;
    //该请求对应Controller的方法
    private Method controllerMethod;

    public Handler(Object controller, Method controllerMethod) {
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
