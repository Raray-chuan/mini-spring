package com.xichuan.framework.core.helper;


import com.xichuan.framework.ConfigConstant;
import java.util.Properties;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description 属性文件助手类
 */
public final class ConfigHelper {

    /**
     * 加载配置文件的属性
     */
    private static final Properties CONFIG_PROPS = PropertiesUtil.loadProps(ConfigConstant.CONFIG_FILE);

    /**
     * 获取应用基础包名
     */
    public static String getAppBasePackage() {
        return PropertiesUtil.getString(CONFIG_PROPS, ConfigConstant.BASE_PACKAGE);
    }

    /**
     * 根据属性名获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropertiesUtil.getString(CONFIG_PROPS, key);
    }

    /**
     * 根据属性名获取 int 类型的属性值
     */
    public static int getInt(String key) {
        return PropertiesUtil.getInt(CONFIG_PROPS, key);
    }

    /**
     * 根据属性名获取 boolean 类型的属性值
     */
    public static boolean getBoolean(String key) {
        return PropertiesUtil.getBoolean(CONFIG_PROPS, key);
    }
}
