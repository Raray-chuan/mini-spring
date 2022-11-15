package com.xichuan.framework.web.helper.argumentHelper;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @Author Xichuan
 * @Date 2022/5/13 20:25
 * @Description HttpSession的Argument处理
 */
public class HttpSessionArgumentResolver implements ArgumentResolver {
    
    public boolean support(Class<?> type, int paramIndex, Method method) {
        return HttpSession.class.isAssignableFrom(type);
    }
    
    public Object argumentResolver(HttpServletRequest request,
                                   HttpServletResponse response, Class<?> type, int paramIndex,
                                   Method method) {
        return request.getSession();
    }
    
}
