package com.xichuan.framework.core.interfaces;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * Spring容器中完成bean实例化、配置以及其他初始化方法前后要添加一些自己逻辑处理
 */
public interface BeanPostProcessor {

    //注入之前处理
    Object postProcessBeforeInitialization(Object bean, String beanName);

    //注入之后处理
    Object postProcessAfterInitialization(Object bean, String beanName);
}
