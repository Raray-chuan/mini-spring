package com.xichuan.dev.boot.aspect;

import com.xichuan.framework.core.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Xichuan
 * @Date 2022/5/13 9:40
 * @Description
 */
@Component
@Aspect
public class BootMethodAspect {
    @PointCut("com.xichuan.dev.boot.service.impl.StudentBootServiceImpl.study()")
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
