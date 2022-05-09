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
    private Object Controller;
    //该请求对应Controller的方法
    private Method ControllerMethod;

    public Handler(Object controller, Method controllerMethod) {
        Controller = controller;
        ControllerMethod = controllerMethod;
    }

    public Object getController() {
        return Controller;
    }

    public void setController(Object controller) {
        Controller = controller;
    }

    public Method getControllerMethod() {
        return ControllerMethod;
    }

    public void setControllerMethod(Method controllerMethod) {
        ControllerMethod = controllerMethod;
    }
}
