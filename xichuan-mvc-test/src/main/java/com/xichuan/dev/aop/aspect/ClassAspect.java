package com.xichuan.dev.aop.aspect;

import com.xichuan.framework.annotation.*;

/**
 * @Author Xichuan
 * @Date 2022/5/10 10:00
 * @Description
 */
@Component
@Aspect
public class ClassAspect {
    @PointCut("com.xichuan.dev.aop.service")
    public void pointcut() {}

    @Before
    public void before() {
        System.out.println("Teacher or student enter class room.");
    }

    @After
    public void after() {
        System.out.println("Teacher or student leave class room.");
    }
}
