package com.xichuan.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * 切面类中，切入点表达式的方法注解
 *
 * 示例一：@PointCut("com.Mytest.service")
 * 对类进行切面，传入的可以是类路径或者是包路径
 * 此时在类执行的前后执行@Before与@After操作
 *
 * 示例二:@PointCut("com.Mytest.service.impl.MethodAspectImpl.methdFunction()")
 *  此对方法进行切面
 *  此时在方法执行前后执行@Before与@After操作
 *
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PointCut {
    String value();
}
