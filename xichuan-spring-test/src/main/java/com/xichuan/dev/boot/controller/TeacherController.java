package com.xichuan.dev.boot.controller;

import com.alibaba.fastjson.JSON;
import com.xichuan.dev.boot.pojo.Student;
import com.xichuan.dev.boot.service.TeacherBootService;
import com.xichuan.framework.core.annotation.Autowired;
import com.xichuan.framework.core.annotation.Controller;
import com.xichuan.framework.web.annotation.RequestBody;
import com.xichuan.framework.web.annotation.RequestMapping;
import com.xichuan.framework.web.annotation.RequestParam;
import com.xichuan.framework.web.annotation.ResponseBody;
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
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/teach
    @RequestMapping(value = "/teach",method = RequestMethod.GET)
    public String teach() {
        teacher.teach("math", Arrays.asList("zhangsan,lisi,wangwu,liuliu".split(",")));
        return "test ioc、aop";
    }

    //测试是否通
    //请求地址:http://127.0.0.1:8888/xichuan/teacher/hello/world
    @RequestMapping(value = "hello/world",method = RequestMethod.GET)
    public String hello() {
        return "hello world!";
    }

    //测试@RequestParam以及基本数据类型参数
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/param/test1?aa=xichuan&param2=南京&param3=18
    @RequestMapping(value = "param/test1",method = RequestMethod.GET)
    public String test1(@RequestParam(value = "aa")String param1,String param2,int param3){
        String str = "param1: "+ param1 + ", param2: "+ param2 + ", param3:" + param3;
        System.out.println(str);
        return str;
    }

    //测试post请求的@RequestBody注解
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/param/test2
    //请求的json:{"id":"1","name":"xichuan","age":18,"address":"南京"}
    @RequestMapping(value = "param/test2",method = RequestMethod.POST)
    public String test2(@RequestBody Student stu){
        String str = JSON.toJSONString(stu);
        System.out.println(str);
        return JSON.toJSONString(str);
    }

    //测试是否含有@RequestParam的Map参数，以及List作为参数
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/param/test3?param1={"name":"张三","age":17}&param2={"name":"lisi","age":18}&param3=["a","b","c"]
    @RequestMapping(value = "param/test3",method = RequestMethod.GET)
    public String test3(@RequestParam(value = "param1") Map<String,String> map1, Map<String,String> param2, List<String> param3){
        String str = "param1: " + JSON.toJSONString(map1) + ", param2: " +JSON.toJSONString(param2) + ", param3: " + JSON.toJSONString(param3);
        System.out.println(str);
        return str;
    }

    //测试View请求重定向页面
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/view/test1
    @RequestMapping(value = "/view/test1",method = RequestMethod.GET)
    public View testModelAndView1(String parm) {
        View view=new View("/param.jsp");
        view.addModel("param","小红");
        return view;
    }

    //测试View的请求转发页面
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/view/test2
    @RequestMapping(value = "/view/test2",method = RequestMethod.GET)
    public View testModelAndView2(String parm) {
        View view=new View("param.jsp");
        view.addModel("param","xichuan");
        return view;
    }

    //测试View请求重定向接口
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/view/test3
    @RequestMapping(value = "/view/test3",method = RequestMethod.GET)
    public View testModelAndView3(String parm) {
        View view = new View("/teacher/hello/world");
        return view;
    }

    //测试View的请求转发接口
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/view/test4
    @RequestMapping(value = "/view/test4",method = RequestMethod.GET)
    public View testModelAndView4() {
        View view=new View("teacher/param/test1");
        view.addModel("aa","xichuan");
        view.addModel("param2","南京");
        view.addModel("param3",18);
        return view;
    }

    //测试@ResponseBody注解，返回对象
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/view/test5
    @RequestMapping(value = "/view/test5",method = RequestMethod.GET)
    @ResponseBody
    public Object testModelAndView5() {
       Student student = new Student();
       student.setId("1");
       student.setName("zhangsan");
       student.setAge(18);
       student.setAddress("Nanjing");
       return student;
    }

    //测试无@ResponseBody注解，返回对象
    //请求地址：http://127.0.0.1:8888/xichuan/teacher/view/test6
    @RequestMapping(value = "/view/test6",method = RequestMethod.GET)
    public Object testModelAndView6() {
        Student student = new Student();
        student.setId("2");
        student.setName("lisi");
        student.setAge(19);
        student.setAddress("Suzhou");
        return student;
    }

}
