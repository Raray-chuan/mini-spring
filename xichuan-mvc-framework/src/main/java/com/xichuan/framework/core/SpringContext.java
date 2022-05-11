package com.xichuan.framework.core;



import com.xichuan.framework.core.annotation.ComponentScan;
import com.xichuan.framework.core.helper.ConfigHelper;
import com.xichuan.framework.core.helper.LoadBeanHelper;

import java.util.ResourceBundle;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */
public class SpringContext {

    static {
        ClassLoader classLoader = SpringContext.class.getClassLoader();//拿到应用类加载器
        //给容器类赋值类加载器
        Container.classLoader=classLoader;
    }

    /**
     * 通过@ComponentScan注解获取根路径，从而加载根路径下的所有class
     * @param config
     */
    public SpringContext(Class<?> config) {
        if(Container.singletonObjects.size()==0) {
            //获取根路径
            ComponentScan componentScanAnnotation = (ComponentScan) config.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            //获取packagePath下的所有class，注册到classesHashSet
            LoadBeanHelper.loadAllClass(path);
            //将BeanDefinition、BeforeDelegatedSet、AfterDelegatedSet、BeanPostProcessorList进行注册
            LoadBeanHelper.loadAllBeanDefinition();
            //生产bean,将需要代理的bean进行代理，放到一级缓存中
            LoadBeanHelper.productBean();
        }
    }

    /**
     * 根据bean的包路径作为根路径
     * @param mainClass
     * @param NUll 无意义字段
     */
    public SpringContext(Class<?> mainClass,String NUll){
        if(Container.singletonObjects.size()==0) {
            //获取根路径
            String path = mainClass.getPackage().getName();
            //获取packagePath下的所有class，注册到classesHashSet
            LoadBeanHelper.loadAllClass(path);
            //将BeanDefinition、BeforeDelegatedSet、AfterDelegatedSet、BeanPostProcessorList进行注册
            LoadBeanHelper.loadAllBeanDefinition();
            //生产bean,将需要代理的bean进行代理，放到一级缓存中
            LoadBeanHelper.productBean();
        }
    }

    /**
     * 通过config.properties配置文件，来加载根路径，从而加载根路径下的所有class
     */
    public SpringContext() {
        if(Container.singletonObjects.size()==0) {
            //获取packagePath下的所有class，注册到classesHashSet
            LoadBeanHelper.loadAllClass(ConfigHelper.getAppBasePackage());
            //将BeanDefinition、BeforeDelegatedSet、AfterDelegatedSet、BeanPostProcessorList进行注册
            LoadBeanHelper.loadAllBeanDefinition();
            //生产单例bean,将需要代理的bean进行代理，放到一级缓存中
            LoadBeanHelper.productBean();
        }

    }

    /**
     * 通过Class获取bean
     * @param s
     * @param <T>
     * @return
     */
    public <T>  T getBean(Class<T> s) {
        String[] name=s.getName().split("\\.");
        return (T) LoadBeanHelper.getBean(name[name.length-1]);
    }

    /**
     * 通过beanNane获取bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return LoadBeanHelper.getBean(beanName);
    }
}
