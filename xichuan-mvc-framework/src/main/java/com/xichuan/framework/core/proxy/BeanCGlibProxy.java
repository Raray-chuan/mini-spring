package com.xichuan.framework.core.proxy;

/**
 * @Author Xichuan
 * @Date 2022/5/10 13:33
 * @Description
 */

import com.xichuan.framework.core.data.MethodNode;
import com.xichuan.framework.core.Container;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * CGlib 动态代理工具类
 */
public class BeanCGlibProxy {

    //有@Aspect注解的@Before前置处理
    private ArrayList<MethodNode> beforeMethodCache;
    //有@Aspect注解的@After后置处理
    private ArrayList<MethodNode> afterMethodCache;

    /**
     * 初始化代理对象class
     * @param targetClass
     * @return
     */
    public Object creatCarProxy(Class targetClass) {
        try {
            Object proxy = this.createProxy(targetClass);
            return proxy;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入一个目标类, 输出一个代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T createProxy(final Class<?> targetClass) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            /**
             * 代理方法, 每次调用目标方法时都会先创建一个 ProxyChain 对象, 然后调用该对象的 doProxyChain() 方法.
             */
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return doProxyChain(targetObject,targetMethod,methodParams,methodProxy);
            }
        });
    }

    /**
     * 进行拦截执行
     * @param targetObject 被代理对象
     * @param targetMethod 被代理的方法
     * @param methodParams 方法参数
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    public Object doProxyChain(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
        Object methodResult;

        before(targetMethod.getName());
        //目标方法执行
        methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        after(targetMethod.getName());

        return methodResult;
    }

    /**
     * 方法执行前执行
     * @param methodName
     * @throws Throwable
     */
    public void before(String methodName) throws Throwable{
        //整个类级别的切面Before方法执行
        if(beforeMethodCache!=null) {
            for (MethodNode beforeMethod : beforeMethodCache) {
                if ((!beforeMethod.isFunction())) {
                    String[] name = beforeMethod.getMethod().getDeclaringClass().toString().split("\\.");
                    beforeMethod.getMethod().invoke(Container.singletonObjects.get(name[name.length - 1]));
                }
            }
        }

        //特定方法Before执行
        if(beforeMethodCache!=null) {
            for (MethodNode beforeMethod : beforeMethodCache) {
                if (beforeMethod.isFunction() && beforeMethod.getMethodName().equals(methodName)) {
                    String[] name = beforeMethod.getMethod().getDeclaringClass().toString().split("\\.");
                    beforeMethod.getMethod().invoke(Container.singletonObjects.get(name[name.length - 1]));
                }
            }
        }
    }

    /**
     * 方法执行后执行
     * @param methodName
     * @throws Throwable
     */
    public void after(String methodName) throws Throwable{
        //特定方法After执行
        if(afterMethodCache!=null) {
            for (MethodNode afterMethod : afterMethodCache) {
                if (afterMethod.isFunction() && afterMethod.getMethodName().equals(methodName)) {
                    String[] name = afterMethod.getMethod().getDeclaringClass().toString().split("\\.");
                    afterMethod.getMethod().invoke(Container.singletonObjects.get(name[name.length - 1]));
                }
            }
        }

        //类级别After执行
        if(afterMethodCache!=null) {
            for (MethodNode afterMethod : afterMethodCache) {
                if ((!afterMethod.isFunction())) {
                    String[] name = afterMethod.getMethod().getDeclaringClass().toString().split("\\.");
                    afterMethod.getMethod().invoke(Container.singletonObjects.get(name[name.length - 1]));
                }
            }
        }
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
