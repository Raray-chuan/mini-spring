package com.xichuan.dev.boot.controller;

import com.xichuan.dev.boot.service.TeacherBootService;
import com.xichuan.framework.core.annotation.Autowired;
import com.xichuan.framework.core.annotation.Controller;
import com.xichuan.framework.web.annotation.RequestMapping;
import com.xichuan.framework.web.data.View;
import com.xichuan.framework.web.data.RequestMethod;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:33
 * @Description
 */
@Controller
public class TeacherController {

    @Autowired
    TeacherBootService teacher;

    @RequestMapping(value = "/teach",method = RequestMethod.GET)
    public String teach() {
        return "名字是";
    }


    @RequestMapping(value = "second",method = RequestMethod.GET)//second?name="xxx"
    public View secondeHandler(String parm)
    {
        View view=new View("/runthrid");
        view.addModel("name","小红");
        return view;
    }
    @RequestMapping(value = "test",method = RequestMethod.GET)
    public String third()
    {
        return "小明和小红";
    }
}