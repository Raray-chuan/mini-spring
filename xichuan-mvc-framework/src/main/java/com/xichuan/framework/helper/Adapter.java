package com.xichuan.framework.helper;


/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

import com.xichuan.framework.data.Handler;
import com.xichuan.framework.data.RequestParam;
import com.xichuan.framework.proxyUtils.MvcProxy;

/**
 * Controller请求处理的适配类
 */
public class Adapter {

    /**
     * 调用controller的对应的方法
     * @param requestParam
     * @param handler
     * @return
     */
        public static Object adapterForRequest(RequestParam requestParam, Handler handler) {
            Object result=null;
            if(requestParam.getParamMap().size()>0) {
                return result= MvcProxy.invokeMethod(handler.getController(),handler.getControllerMethod(),requestParam.getParamMap());
            }else {
                return result=MvcProxy.invokeMethod(handler.getController(),handler.getControllerMethod(),null);
            }
        }
}
