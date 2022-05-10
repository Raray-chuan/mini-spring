package com.xichuan.framework.proxyUtils;


import com.xichuan.framework.data.BeanDefinition;
import com.xichuan.framework.data.MethodNode;
import com.xichuan.framework.proxyUtils.BeanCGlibProxyUtil;
import com.xichuan.framework.proxyUtils.BeanJDKProxyUtil;

import java.util.ArrayList;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * 动态代理工厂类
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
    //
    private ArrayList<MethodNode> beforeMethodCache;
    private ArrayList<MethodNode> afterMethodCache;

    //是否使用cglib
    private boolean isCGlib = true;

    /**
     * 此方法用于返回是不是动态代理对象
     * @return
     */
    public Object getTarget() {
        if (isDelegated && proxyInstance == null) {
            //创建代理工具类
            if(isCGlib){
                BeanCGlibProxyUtil beanCGlibProxyUtil = new BeanCGlibProxyUtil();
                beanCGlibProxyUtil.setAfterMethodCache(getAfterMethodCache());
                beanCGlibProxyUtil.setBeforeMethodCache(getBeforeMethodCache());
                proxyInstance = beanCGlibProxyUtil.creatCarProxy(clazz);
            }else{
                BeanJDKProxyUtil beanJDKProxyUtil = new BeanJDKProxyUtil();
                beanJDKProxyUtil.setAfterMethodCache(getAfterMethodCache());
                beanJDKProxyUtil.setBeforeMethodCache(getBeforeMethodCache());
                proxyInstance = beanJDKProxyUtil.creatCarProxy(instance);
            }
            return proxyInstance;
        }else if(isDelegated && proxyInstance != null){
            return proxyInstance;
        } else {  //不代理的话直接返回
            return instance;

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
