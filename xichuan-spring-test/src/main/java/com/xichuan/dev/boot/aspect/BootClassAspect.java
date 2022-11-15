package com.xichuan.dev.boot.aspect;

import com.xichuan.framework.core.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Xichuan
 * @Date 2022/5/13 9:41
 * @Description
 */
@Component
@Aspect
public class BootClassAspect {
    @PointCut("com.xichuan.dev.boot.service.impl.TeacherBootServiceImpl")
    public void pointcut() {}

    @Before
    public void before() {
        System.out.println("----Teach start teach time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    @After
    public void after() {
        System.out.println("----Teach end teach time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
