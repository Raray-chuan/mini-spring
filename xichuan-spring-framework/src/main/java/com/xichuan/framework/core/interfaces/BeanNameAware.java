package com.xichuan.framework.core.interfaces;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description 让Bean获取自己在BeanFactory配置中的名字（根据情况是id或者name）
 */
public interface BeanNameAware {
    public void setBeanName(String BeanName);
}
