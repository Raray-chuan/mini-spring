package com.xichuan.framework.helper;

import java.util.Collection;
import java.util.Properties;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */
public class Utils {
    //获取名词
    public static String getSystem() {
        Properties props = System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        return osName;
    }

    /**
     * 判断字符串是否为null
     * @param str
     * @return
     */
    public static Boolean isNotBlack(String str){
        if (str == null || "".equals(str)){
            return false;
        }else{
            return true;
        }
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

}
