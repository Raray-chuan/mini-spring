package com.xichuan.framework.web.data;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description Controller中含有@RequestMapping的都封装成一个Request，主要存放uri以及请求类型
 */
public class Request {

    //请求的uri
    private String requestPath;

    //请求的类型，GET、POST....
    private String requestMethod;

    public Request(String requestPath, String requestMethod) {
        this.requestPath = requestPath;
        this.requestMethod = requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;

        return this.requestPath.equals(request.requestPath) && this.requestMethod.equals(request.requestMethod);
    }

    @Override
    public int hashCode() {
        return requestPath.hashCode()&requestMethod.hashCode()&21;
    }


    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
}
