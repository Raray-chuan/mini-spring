package com.xichuan.framework.web.tomcat;


import com.xichuan.framework.core.Container;
import com.xichuan.framework.core.helper.PackageUtil;
import com.xichuan.framework.web.data.Request;
import com.xichuan.framework.web.data.RequestHandler;
import com.xichuan.framework.web.data.RequestParam;
import com.xichuan.framework.web.helper.*;
import com.xichuan.framework.web.helper.argumentHelper.ArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * 每一个http请求进行拦截一次（此Servlet对Rest的请求进行拦截）
 * (通过的注解的方式加载Servlet，tomcat会将请求转发此Servlet)
 */
@WebServlet(name = "rest_request",urlPatterns = "/",loadOnStartup = 3)
public class RestRequestServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(RestRequestServlet.class);

    //参数处理集合
    private List<ArgumentResolver> argumentResolvers;

    /**
     * 初始化方法
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) {
        try {
            //初始化ArgumentResolver
            argumentResolvers = findImplementObject(ArgumentResolver.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 请求处理逻辑
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("rest request,request uri:" + request.getRequestURI());

        String requestMethod = request.getMethod(); //请求类型，GET/POST...
        String requestPath = request.getRequestURI();//获取到的路径类似 /aa=xxx

        //交给处理器映射器处理
        RequestHandler requestHandler = HandlerMappingHelper.getRequestHandler(new Request(UrlUtil.formatUrl(requestPath), requestMethod));
        if (requestHandler == null) {
            ViewResolver.handle404Result(request, response);
            return;
        }

        //封装RequestParam
        RequestParam param = new RequestParam();
        param.creatParam(argumentResolvers,requestHandler,request, response);

        //请求处理器适配器适配器适配Param
        Object result = HandlerAdapter.adapterForRequest(param, requestHandler);

        //对对返回的View或者Data进行处理
        ViewAdapter.adapter(result,requestHandler,request,response);
    }


    /**
     * 获取接口对应目录下的，对应的实现类对象
     */
    private static <T> List<T> findImplementObject(Class<?> interfaceClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException, ClassNotFoundException {
        List<T> result = new ArrayList<>();
        Set<Class<?>> classSet = loadAllClass(interfaceClass.getPackage().getName());
        //接口对应的所有实现类
        List<Class<?>> argumentClass = getClassSetBySuper(classSet, interfaceClass);
        for (Class<?> clazz : argumentClass) {
            logger.debug("init argument resolver: " + clazz.getName());
            result.add((T)clazz.getDeclaredConstructor().newInstance());
        }


        return result;
    }

    /**
     * 获取基础包名下某父类的所有子类 或某接口的所有实现类
     *
     * @param classesHashSet 说扫描的class
     * @param superClass     父类class
     * @return
     */
    public static List<Class<?>> getClassSetBySuper(Set<Class<?>> classesHashSet, Class<?> superClass) {
        List<Class<?>> classList = new ArrayList<>();
        for (Class<?> cls : classesHashSet) {
            //isAssignableFrom() 指 superClass 和 cls 是否相同或 superClass 是否是 cls 的父类/接口
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                classList.add(cls);
            }
        }
        return classList;
    }

    /**
     * 获取某包下的所有类
     *
     * @param packagePath
     * @return
     */
    public static Set<Class<?>> loadAllClass(String packagePath) throws IOException, ClassNotFoundException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath.replace(".","/"));
        HashSet<Class<?>> classesHashSet = new HashSet<>();
        Class<?> clazz;
        if (url != null){
            if (url.getProtocol().equals("file")){
                List<String> classNames = PackageUtil.getClassName(packagePath,false);

                for (String className : classNames) {
                    try {
                        clazz = Container.classLoader.loadClass(className);
                        classesHashSet.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (url.getProtocol().equals("jar")) {
                /*String[] jarInfo = url.getPath().split("!");
                String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
                //System.out.println(jarFilePath);
                Enumeration<JarEntry> entrys = new JarFile(jarFilePath).entries();
                JarEntry jarEntry;*/

                /*while (entrys.hasMoreElements()) {
                    jarEntry = entrys.nextElement();
                    String entryName = jarEntry.getName();
                    //System.out.println(entryName);
                    if (entryName.endsWith(".class")) {
                        System.out.println("entryName:" + entryName);
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            clazz = Container.classLoader.loadClass(entryName);
                            classesHashSet.add(clazz);
                        }
                    }
                }*/

                //todo 从jar包中的jar中加载读取class(此处没有想到好的代码实现方法，等以后研究研究如何加载的时候再实现)，此处先写死
                List<String> packageClass = new ArrayList<>();
                packageClass.add("com.xichuan.framework.web.helper.argumentHelper.DefaultArgumentResolver");
                packageClass.add("com.xichuan.framework.web.helper.argumentHelper.HttpServletRequestArgumentResolver");
                packageClass.add("com.xichuan.framework.web.helper.argumentHelper.HttpServletResponseArgumentResolver");
                packageClass.add("com.xichuan.framework.web.helper.argumentHelper.HttpSessionArgumentResolver");
                packageClass.add("com.xichuan.framework.web.helper.argumentHelper.RequestBodyArgumentResolver");
                packageClass.add("com.xichuan.framework.web.helper.argumentHelper.RequestParamArgumentResolver");
                for (String className : packageClass){
                    clazz = Container.classLoader.loadClass(className);
                    classesHashSet.add(clazz);
                }
            }
        }




        return classesHashSet;
    }
}

