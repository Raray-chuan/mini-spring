package com.xichuan.framework.core;

import com.xichuan.framework.core.proxy.DynamicBeanFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description bean实例容器
 */
public class Container {
    //一级，日常实际获取Bean的地方
    public static ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();
    //二级，临时
    public static ConcurrentHashMap<String,Object> earlySingletonObjects = new ConcurrentHashMap<>();
    //三级，Value是一个动态代理对象工厂
    public static ConcurrentHashMap<String, DynamicBeanFactory> singletonFactory = new ConcurrentHashMap<>();
    //Controller对象容器
    public static ConcurrentHashMap<String,Object> controllerMap = new ConcurrentHashMap<>();
    //类加载器,在SpringContext的静态代码块中进行赋值的
    public static ClassLoader classLoader = null;
}
