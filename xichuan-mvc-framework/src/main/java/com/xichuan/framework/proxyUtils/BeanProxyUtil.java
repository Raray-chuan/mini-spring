package com.xichuan.framework.proxyUtils;



import com.xichuan.framework.helper.Container;
import com.xichuan.framework.data.MethodNode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * JavaProxy动态代理工具类
 */
public class BeanProxyUtil implements InvocationHandler {

    //代理对象
    private Object target;
    //有@Aspect注解的@Before前置处理
    private ArrayList<MethodNode> beforeMethodCache;
    //有@Aspect注解的@After后置处理
    private ArrayList<MethodNode> afterMethodCache;

    /**
     * 初始化代理对象
     * @param target
     * @return
     */
    public Object creatCarProxy(Object target) {
        this.target=target;
        try {
            Object proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
            return proxy;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 动态代理的执行
     * @param proxy 代理对象
     * @param method 指定的方法
     * @param args 执行的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result=null;
        //整个类级别的切面Before方法执行
        if(beforeMethodCache!=null)
            for(MethodNode beforeMethod:beforeMethodCache) {
                if((!beforeMethod.isFunction())) {
                    String[] name=beforeMethod.getMethod().getDeclaringClass().toString().split("\\.");
                    beforeMethod.getMethod().invoke(Container.singletonObjects.get(name[name.length-1]));
                }
            }


        //特定方法Before执行
        if(beforeMethodCache!=null)
            for(MethodNode beforeMethod:beforeMethodCache) {
                if(beforeMethod.isFunction()&&beforeMethod.getMethodName().equals(method.getName())) {
                    String[] name=beforeMethod.getMethod().getDeclaringClass().toString().split("\\.");
                    beforeMethod.getMethod().invoke(Container.singletonObjects.get(name[name.length-1]));
                }
            }

        //目标方法执行
        result=method.invoke(target,args);

        //特定方法After执行
        if(afterMethodCache!=null)
            for(MethodNode afterMethod:afterMethodCache) {
                if(afterMethod.isFunction()&&afterMethod.getMethodName().equals(method.getName())) {
                    String[] name=afterMethod.getMethod().getDeclaringClass().toString().split("\\.");
                    afterMethod.getMethod().invoke(Container.singletonObjects.get(name[name.length-1]));
                }
            }

        //类级别After执行
        if(afterMethodCache!=null)
            for(MethodNode afterMethod:afterMethodCache) {
                if((!afterMethod.isFunction())) {
                    String[] name=afterMethod.getMethod().getDeclaringClass().toString().split("\\.");
                    afterMethod.getMethod().invoke(Container.singletonObjects.get(name[name.length-1]));
                }
            }

        return  result;
    }


    public ArrayList<MethodNode> getBeforeMethodCache() {
        return beforeMethodCache;
    }

    public void setBeforeMethodCache(ArrayList<MethodNode> beforeMethodCache) {
        this.beforeMethodCache = beforeMethodCache;
    }

    public ArrayList<MethodNode> getAfterMethodCache() {
        return afterMethodCache;
    }

    public void setAfterMethodCache(ArrayList<MethodNode> afterMethodCache) {
        this.afterMethodCache = afterMethodCache;
    }

}
