package com.xichuan.dev.ioc;

import com.xichuan.dev.config.ScanConfig;
import com.xichuan.dev.ioc.service.TeacherIocService;
import com.xichuan.framework.core.SpringContext;

import java.util.Arrays;

/**
 * @Author Xichuan
 * @Date 2022/5/10 9:41
 * @Description
 */
public class TestIOC {
/*
    public static void main(String[] args) {
        testDemo();
    }
*/

    public static void testDemo(){
        //通过@ComponentScan获取根路径
        //SpringContext app = new SpringContext(ScanConfig.class);
        //通过config.properties获取根路径
        SpringContext app = new SpringContext();
        TeacherIocService teacher = (TeacherIocService)app.getBean("teacherService");
        teacher.teach("语文", Arrays.asList("zhangsan,lisi,wangwu,liuliu".split(",")));
    }
}
