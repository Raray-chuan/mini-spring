package com.xichuan.dev.aop;



import com.xichuan.dev.aop.service.TeacherAopService;
import com.xichuan.dev.aop.service.TeacherAopServiceImpl;
import com.xichuan.dev.config.ScanConfig;
import com.xichuan.framework.core.SpringContext;

import java.util.Arrays;

/**
 * @Author Xichuan
 * @Date 2022/5/10 10:02
 * @Description
 */
public class TestAOP {
    public static void main(String[] args) {
        //通过@ComponentScan获取根路径
        SpringContext app = new SpringContext(ScanConfig.class);
        //通过config.properties获取根路径
        //SpringApplication app = new SpringApplication();
        TeacherAopService teacher = app.getBean(TeacherAopServiceImpl.class);
        teacher.teach("math", Arrays.asList("zhangsan,lisi,wangwu,liuliu".split(",")));
    }
}
