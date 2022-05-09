package com.xichuan.framework.config;



import com.xichuan.framework.helper.HandlerMapping;
import com.xichuan.framework.helper.LoadBeanHelper;

import java.util.ResourceBundle;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * DispatcherServlet的初始化方法
 */
public class InitMethod {
    private static boolean isLoaded=false;

    public static void init(){
        if(isLoaded)
            return;
        ResourceBundle bundle = ResourceBundle.getBundle("config");
        String s=bundle.getString("componentScan");
        LoadBeanHelper.LoadAllClass(bundle.getString("componentScan"));
        LoadBeanHelper.LoadAllBean();
        LoadBeanHelper.ProductBean();
        HandlerMapping.getAllHandler();
        isLoaded=true;
    }
}
