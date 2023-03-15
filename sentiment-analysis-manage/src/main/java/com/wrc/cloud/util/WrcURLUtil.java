package com.wrc.cloud.util;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/5 14:25
 */
public class WrcURLUtil {
    public static String getUrlWithoutParam(String url){
        url = url.split("#")[0];
        url = url.split("\\?")[0];
        return url;
    }
}
