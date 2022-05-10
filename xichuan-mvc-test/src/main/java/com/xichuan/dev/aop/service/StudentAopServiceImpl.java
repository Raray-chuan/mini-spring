package com.xichuan.dev.aop.service;

import com.xichuan.framework.annotation.Service;

/**
 * @Author Xichuan
 * @Date 2022/5/10 9:43
 * @Description
 */
@Service
public class StudentAopServiceImpl implements StudentAopService {

    @Override
    public void study(String studentName) {
        System.out.println(studentName + " is start study!");
    }
}
