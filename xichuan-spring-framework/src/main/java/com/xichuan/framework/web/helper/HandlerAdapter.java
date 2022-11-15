package com.xichuan.framework.web.helper;

import com.xichuan.framework.web.data.RequestHandler;
import com.xichuan.framework.web.data.RequestParam;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description Controller请求处理的适配类
 */
public class HandlerAdapter {

    /**
     * 调用controller的对应的方法
     * @param requestParam
     * @param requestHandler
     * @return
     */
        public static Object adapterForRequest(RequestParam requestParam, RequestHandler requestHandler) {
            if(requestParam.getParams() != null && requestParam.getParams().length>0) {
                return MvcProxy.invokeMethod(requestHandler.getController(), requestHandler.getControllerMethod(),requestParam.getParams());
            }else {
                return MvcProxy.invokeMethod(requestHandler.getController(), requestHandler.getControllerMethod(),null);
            }
        }
}
