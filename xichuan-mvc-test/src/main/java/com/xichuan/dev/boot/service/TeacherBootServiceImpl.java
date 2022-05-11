package com.xichuan.dev.boot.service;

import com.xichuan.framework.core.annotation.Autowired;
import com.xichuan.framework.core.annotation.Service;

import java.util.List;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:50
 * @Description
 */
@Service
public class TeacherBootServiceImpl implements TeacherBootService{
    @Autowired("bootStudentService")
    StudentBootService student;

    @Override
    public void teach(String subject, List<String> students) {
        System.out.println("teacher start teach subject: " + subject);
        for (String s : students){
            student.study(s);
        }
    }
}
