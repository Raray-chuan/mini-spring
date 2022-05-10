package com.xichuan.dev.config;

import com.xichuan.framework.annotation.Component;
import com.xichuan.framework.interfaces.BeanPostProcessor;

/**
 * @Author Xichuan
 * @Date 2022/5/10 10:16
 * @Description
 */

/**
 * 如果要测试BeanPostProcessor，可以将@Component注解打开
 */
@Component
public class ProcessorConfig implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("Spring bean '" + beanName+"' is start init.");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("Spring bean '" + beanName+"' is finish init.");
        return bean;
    }
}
