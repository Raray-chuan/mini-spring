package com.xichuan.dev.boot.service;

import java.util.List;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:49
 * @Description
 */
public interface TeacherBootService {
    void teach(String subject, List<String> students);
}
