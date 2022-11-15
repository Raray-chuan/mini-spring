package com.xichuan.framework.core.data;

import java.lang.reflect.Method;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * 保存切面类与被切面类的一些信息
 */
public class MethodNode {

    //切面类的方法，加了@After或@Before注解
    private Method method;

    //是否对方法进行切面
    private boolean isFunction;

    //如果是方法切面，被切面的方法名词
    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MethodNode(Method method, boolean isFunction) {
        this.method = method;
        this.isFunction = isFunction;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public void setFunction(boolean function) {
        isFunction = function;
    }
}
