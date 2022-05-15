package com.xichuan.dev.boot.controller;

import com.alibaba.fastjson.JSON;
import com.xichuan.dev.boot.pojo.Student;
import com.xichuan.dev.boot.service.TeacherBootService;
import com.xichuan.framework.core.annotation.Autowired;
import com.xichuan.framework.core.annotation.Controller;
import com.xichuan.framework.web.annotation.RequestBody;
import com.xichuan.framework.web.annotation.RequestMapping;
import com.xichuan.framework.web.annotation.RequestParam;
import com.xichuan.framework.web.data.View;
import com.xichuan.framework.web.data.RequestMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "test1",method = RequestMethod.GET)
    public String test1(@RequestParam(value = "aa")String param1,String param2,int param3){
        System.out.println("param1: "+ param1 + ", param2: "+ param2 + ", param3:" + param3);
        return "test1";
    }

    @RequestMapping(value = "test2",method = RequestMethod.POST)
    public String test2(@RequestBody Student stu){
        System.out.println(JSON.toJSONString(stu));
        return JSON.toJSONString(stu);
    }

    @RequestMapping(value = "test3",method = RequestMethod.GET)
    public String test3(@RequestParam(value = "param1") Map<String,String> map1, Map<String,String> param2, List<String> param3){
        System.out.println("test3");
        return "test3";
    }

}
//JAVA8反射获取方法参数名
//https://blog.csdn.net/ilovewqf/article/details/103198763