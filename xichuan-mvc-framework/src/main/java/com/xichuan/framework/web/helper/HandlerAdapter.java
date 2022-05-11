package com.xichuan.framework.web.helper;


/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

import com.xichuan.framework.web.data.RequestHandler;
import com.xichuan.framework.web.data.RequestParam;

/**
 * Controller请求处理的适配类
 */
public class HandlerAdapter {

    /**
     * 调用controller的对应的方法
     * @param requestParam
     * @param requestHandler
     * @return
     */
        public static Object adapterForRequest(RequestParam requestParam, RequestHandler requestHandler) {
            Object result=null;
            if(requestParam.getParamMap().size()>0) {
                return MvcProxy.invokeMethod(requestHandler.getController(), requestHandler.getControllerMethod(),requestParam.getParamMap());
            }else {
                return MvcProxy.invokeMethod(requestHandler.getController(), requestHandler.getControllerMethod(),null);
            }
        }
}
