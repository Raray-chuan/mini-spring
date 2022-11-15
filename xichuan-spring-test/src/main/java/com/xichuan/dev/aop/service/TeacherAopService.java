package com.xichuan.dev.aop.service;

import java.util.List;

/**
 * @Author Xichuan
 * @Date 2022/5/10 9:44
 * @Description
 */
public interface TeacherAopService {
    void teach(String subject, List<String> students);
}
