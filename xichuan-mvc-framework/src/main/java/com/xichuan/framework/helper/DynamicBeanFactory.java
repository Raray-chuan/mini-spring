package com.xichuan.framework.helper;


import com.xichuan.framework.data.BeanDefinition;
import com.xichuan.framework.data.MethodNode;
import com.xichuan.framework.proxyUtils.BeanProxyUtil;

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
    //Bean实例化对象
    private Object instance;
    //是否动态代理
    private Boolean isDelegated=false;
    //
    private ArrayList<MethodNode> beforeMethodCache;
    private ArrayList<MethodNode> afterMethodCache;

    /**
     * 此方法用于返回是不是动态代理对象
     * @return
     */
    public Object getTarget() {
        if (isDelegated) {
            //创建代理工具类
            BeanProxyUtil beanProxyUtil = new BeanProxyUtil();
            beanProxyUtil.setAfterMethodCache(getAfterMethodCache());
            beanProxyUtil.setBeforeMethodCache(getBeforeMethodCache());
            return beanProxyUtil.creatCarProxy(instance);
        } else {  //不代理的话直接返回
            return instance;

        }
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
