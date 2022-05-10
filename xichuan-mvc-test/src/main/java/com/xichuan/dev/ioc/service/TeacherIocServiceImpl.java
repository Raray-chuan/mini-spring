package com.xichuan.dev.ioc.service;

import com.xichuan.framework.annotation.Autowired;
import com.xichuan.framework.annotation.Service;

import java.util.List;

/**
 * @Author Xichuan
 * @Date 2022/5/10 9:55
 * @Description
 */
@Service("teacherService")
public class TeacherIocServiceImpl implements TeacherIocService {

    @Autowired("studentService")
    StudentIocServiceImpl student;

    @Override
    public void teach(String subject, List<String> students) {
        System.out.println("teacher start teach subject: " + subject);
        for (String s : students){
            student.study(s);
        }
    }
}
