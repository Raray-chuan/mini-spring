package com.xichuan.framework.web.helper;




import com.xichuan.framework.core.helper.ConfigHelper;
import com.xichuan.framework.core.helper.LoadBeanHelper;
import com.xichuan.framework.core.helper.Utils;

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
    //是否被加载过
    private static boolean isLoaded = false;

    /**
     * 初始化bean
     * @param basePackage
     */
    public static void init(String basePackage){
        //判断是否加载过
        if(isLoaded) {
            return;
        }

        if (!Utils.isNotBlack(basePackage)){
            basePackage = ConfigHelper.getAppBasePackage();
        }

        LoadBeanHelper.loadAllClass(basePackage);
        LoadBeanHelper.loadAllBeanDefinition();
        LoadBeanHelper.productBean();
        HandlerMapping.getAllHandler();
        isLoaded=true;
    }
}
