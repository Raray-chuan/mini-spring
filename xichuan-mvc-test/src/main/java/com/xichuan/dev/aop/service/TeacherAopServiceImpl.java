package com.xichuan.dev.aop.service;

import com.xichuan.framework.annotation.Autowired;
import com.xichuan.framework.annotation.Service;

import java.util.List;

/**
 * @Author Xichuan
 * @Date 2022/5/10 9:55
 * @Description
 */
@Service
public class TeacherAopServiceImpl implements TeacherAopService {

    @Autowired
    StudentAopService student;

    @Override
    public void teach(String subject, List<String> students) {
        System.out.println("teacher start teach subject: " + subject);
        for (String s : students){
            student.study(s);
        }
    }
}
