package com.xichuan.framework.data;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * Controller的一个Request封装类
 */
public class Request {
    //请求的uri
    private String RequestPath;
    //请求的类型，GET、POST....
    private String RequestMethod;

    public Request(String requestPath, String requestMethod) {
        RequestPath = requestPath;
        RequestMethod = requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;

        return RequestPath.equals(request.RequestPath) && RequestMethod.equals(request.RequestMethod);
    }

    @Override
    public int hashCode() {
        return RequestPath.hashCode()&RequestMethod.hashCode()&21;
    }



    public String getRequestPath() {
        return RequestPath;
    }

    public void setRequestPath(String requestPath) {
        RequestPath = requestPath;
    }

    public String getRequestMethod() {
        return RequestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        RequestMethod = requestMethod;
    }
}
