package com.xichuan.dev.boot.service.impl;

import com.xichuan.dev.boot.service.StudentBootService;
import com.xichuan.framework.core.annotation.Service;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:49
 * @Description
 */
@Service("bootStudentService")
public class StudentBootServiceImpl implements StudentBootService {
    @Override
    public void study(String studentName) {
        System.out.println(studentName + " is start study!");
    }
}
