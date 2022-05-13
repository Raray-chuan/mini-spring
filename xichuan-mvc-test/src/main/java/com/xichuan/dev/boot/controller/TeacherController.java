package com.xichuan.dev.boot.controller;

import com.xichuan.dev.boot.service.TeacherBootService;
import com.xichuan.framework.core.annotation.Autowired;
import com.xichuan.framework.core.annotation.Controller;
import com.xichuan.framework.web.annotation.RequestMapping;
import com.xichuan.framework.web.data.View;
import com.xichuan.framework.web.data.RequestMethod;

import java.util.Arrays;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:33
 * @Description
 */
@Controller
@RequestMapping("/teacher/")
public class TeacherController {

    @Autowired
    TeacherBootService teacher;

    //测试ioc、aop
    @RequestMapping(value = "/teach",method = RequestMethod.GET)
    public String teach() {
        teacher.teach("math", Arrays.asList("zhangsan,lisi,wangwu,liuliu".split(",")));
        return "test ioc、aop";
    }

    @RequestMapping(value = "hello/world",method = RequestMethod.GET)
    public String hello() {
        return "hello world!";
    }

    @RequestMapping(value = "/view/one",method = RequestMethod.GET)//second?name="xxx"
    public View testModelAndView1(String parm) {
        View view=new View("/runthrid");
        view.addModel("name","小红");
        return view;
    }

    @RequestMapping(value = "test",method = RequestMethod.GET)
    public String test() {

        return "this is a test message.";
    }
}
