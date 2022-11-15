package com.xichuan.framework.core.proxy;


import com.xichuan.framework.core.data.BeanDefinition;
import com.xichuan.framework.core.data.MethodNode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description 动态代理工厂类
 */
public class DynamicBeanFactory {
    //Bean定义信息
    private BeanDefinition beanDefinition;

    //被代理对象的class
    private Class clazz;

    //Bean实例化对象(原始对象)
    private Object instance;

    //代理对象
    private Object proxyInstance;

    //是否动态代理
    private Boolean isDelegated=false;

    //aop的前置处理方法与后置处理方法
    private ArrayList<MethodNode> beforeMethodCache;
    private ArrayList<MethodNode> afterMethodCache;

    //是否使用cglib
    private boolean isCGlib = true;

    /**
     * 创建实例对象或者代理对象
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public Object createInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //如果是CGlib，则只需要创建代理类即可
        if (isDelegated && proxyInstance == null) {
            //创建代理工具类
            if(isCGlib){
                BeanCGlibProxy beanCGlibProxy = new BeanCGlibProxy();
                beanCGlibProxy.setAfterMethodCache(getAfterMethodCache());
                beanCGlibProxy.setBeforeMethodCache(getBeforeMethodCache());
                proxyInstance = beanCGlibProxy.creatCarProxy(clazz);
            }else{
                instance = clazz.getDeclaredConstructor().newInstance();
                BeanJDKProxy beanJDKProxy = new BeanJDKProxy();
                beanJDKProxy.setAfterMethodCache(getAfterMethodCache());
                beanJDKProxy.setBeforeMethodCache(getBeforeMethodCache());
                proxyInstance = beanJDKProxy.creatCarProxy(instance);
            }
            return proxyInstance;
        }else if(isDelegated && proxyInstance != null){
            return proxyInstance;
        } else {  //不代理的话直接返回
            instance = clazz.getDeclaredConstructor().newInstance();
            return instance;
        }
    }

    /**
     * 返回实例对象或者代理对象
     * @return
     */
    public Object getTarget() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //代理的话返回代理对象，不代理的话返回本身对象
        if (isDelegated) {
            return proxyInstance == null ? createInstance() : proxyInstance;
        }else {
            return instance == null ? createInstance() : instance;
        }
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isCGlib() {
        return isCGlib;
    }

    public void setCGlib(boolean CGlib) {
        isCGlib = CGlib;
    }

    public Object getProxyInstance() {
        return proxyInstance;
    }

    public void setProxyInstance(Object proxyInstance) {
        this.proxyInstance = proxyInstance;
    }

    public Boolean getDelegated() {
        return isDelegated;
    }

    public void setDelegated(Boolean delegated) {
        isDelegated = delegated;
    }

    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public void setBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
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
