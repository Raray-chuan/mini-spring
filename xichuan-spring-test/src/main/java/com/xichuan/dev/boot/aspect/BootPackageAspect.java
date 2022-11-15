package com.xichuan.dev.boot.aspect;

import com.xichuan.framework.core.annotation.*;

/**
 * @Author Xichuan
 * @Date 2022/5/13 9:40
 * @Description
 */
@Component
@Aspect
public class BootPackageAspect {
    @PointCut("com.xichuan.dev.boot.service")
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
