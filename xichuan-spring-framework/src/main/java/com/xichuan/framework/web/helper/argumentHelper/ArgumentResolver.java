package com.xichuan.framework.web.helper.argumentHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Author Xichuan
 * @Date 2022/5/13 20:25
 * @Description
 */

/**
 * Argument处理接口
 */
public interface ArgumentResolver {
    
    public boolean support(Class<?> type, int paramIndex, Method method);
    
    public Object argumentResolver(HttpServletRequest request,
                                   HttpServletResponse response, Class<?> type, int paramIndex,
                                   Method method) throws IOException;
}
