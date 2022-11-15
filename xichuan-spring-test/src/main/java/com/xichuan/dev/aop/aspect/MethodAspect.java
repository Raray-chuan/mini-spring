package com.xichuan.dev.aop.aspect;


import com.xichuan.framework.core.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Xichuan
 * @Date 2022/5/10 10:00
 * @Description
 */
@Component
@Aspect
public class MethodAspect {

    @PointCut("com.xichuan.dev.aop.service.StudentAopServiceImpl.study()")
    public void pointcut() {}

    @Before
    public void before() {
        System.out.println("----Student start study time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    @After
    public void after() {
        System.out.println("----Student end study time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
