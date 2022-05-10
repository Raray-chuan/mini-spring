package com.xichuan.dev.ioc;

import com.xichuan.dev.config.ScanConfig;
import com.xichuan.dev.ioc.service.TeacherIocService;
import com.xichuan.dev.ioc.service.TeacherIocServiceImpl;
import com.xichuan.framework.SpringApplication;

import java.util.Arrays;

/**
 * @Author Xichuan
 * @Date 2022/5/10 9:41
 * @Description
 */
public class TestIOC {
    public static void main(String[] args) {
        //通过@ComponentScan获取根路径
        SpringApplication app = new SpringApplication(ScanConfig.class);
        //通过config.properties获取根路径
        //SpringApplication app = new SpringApplication();
        TeacherIocService teacher = app.getBean(TeacherIocServiceImpl.class);
        teacher.teach("语文", Arrays.asList("zhangsan,lisi,wangwu,liuliu".split(",")));


    }
}
