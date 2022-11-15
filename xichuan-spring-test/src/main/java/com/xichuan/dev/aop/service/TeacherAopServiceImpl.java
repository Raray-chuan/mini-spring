package com.xichuan.dev.aop.service;

import com.xichuan.framework.core.annotation.Autowired;
import com.xichuan.framework.core.annotation.Service;

import java.util.List;

/**
 * @Author Xichuan
 * @Date 2022/5/10 9:55
 * @Description
 */
@Service
public class TeacherAopServiceImpl implements TeacherAopService {

    public TeacherAopServiceImpl(){
        System.out.println("init TeacherAopServiceImpl.....");
    }

    @Autowired
    StudentAopServiceImpl student;

    @Override
    public void teach(String subject, List<String> students) {
        System.out.println("teacher start teach subject: " + subject);
        for (String s : students){
            student.study(s);
        }
    }
}
