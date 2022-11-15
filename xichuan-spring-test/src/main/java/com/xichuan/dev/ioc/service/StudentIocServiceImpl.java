package com.xichuan.dev.ioc.service;

import com.xichuan.framework.core.annotation.Service;

/**
 * @Author Xichuan
 * @Date 2022/5/10 9:43
 * @Description
 */
@Service("studentService")
public class StudentIocServiceImpl implements StudentIocService {

    @Override
    public void study(String studentName) {
        System.out.println(studentName + " is start study!");
    }
}
