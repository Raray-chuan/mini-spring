package com.xichuan.framework.web.helper;

import com.xichuan.framework.core.helper.Utils;

/**
 * @Author Xichuan
 * @Date 2022/5/11 17:50
 * @Description URL处理工具类
 */
public class UrlUtil {

    private static String URL_SPLIT =  "/";
    private static String URL_PATTERN_ALL = "*";

    /**
     * 格式化URL
     * @param url
     * @return
     */
    public static String formatUrl(String url){
        String formatUrl = "";
        if (!Utils.isNotBlack(url) || URL_SPLIT.equals(url)){
            return url;
        }

        formatUrl = URL_SPLIT + url + URL_SPLIT;
        return formatUrl.replace("//",URL_SPLIT);
    }

    /**
     * 获取*
     * @return
     */
    public static String getUrlPatternAll(){
        return URL_PATTERN_ALL;
    }

}
