package com.xichuan.framework.web.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description http请求返回的View封装类
 */
public class View {
    //跳转的请求路径
    private String path= null ;
    //跳转的请求参数
    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<String, Object>();
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
