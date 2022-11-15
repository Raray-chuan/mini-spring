package com.xichuan.framework.core.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Properties;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */
public class Utils {

    //获取系统类型
    public static String getSystem() {
        Properties props = System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        return osName;
    }


    /**
     * 从Linux获取hostname
     * @return
     */
    public static String getHostNameForLiunx() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }

    /**
     * 获取系统的hostname
     * @return
     */
    public static String getHostName() {
        //window中获取COMPUTERNAME
        if (System.getenv("COMPUTERNAME") != null) {
            return System.getenv("COMPUTERNAME");
        } else { //从Linux中获取hostname
            return getHostNameForLiunx();
        }
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
