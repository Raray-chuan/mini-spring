package com.xichuan.framework.core.interfaces;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候会执行该方法。
 */
public interface InitializingBean {
     void afterPropertiesSet() throws Exception;
}
