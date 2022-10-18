package com.micker.rpc.host;

import android.text.TextUtils;

/**
 * Created by zhangyang on 16/6/23.
 */
public class HostManager {
    private static String BaseUrl = "http://api.314.la/tail/";

    public static void setBaseUrl(String BASEURL) {
        HostManager.BaseUrl = BASEURL;
    }

    public static String getBaseUrl() {
        return BaseUrl;
    }


    private static String environment = "prod";

    public static void setEnvironment(String environment) {
        HostManager.environment = environment;
    }

    public static String getEnvironment() {
        return TextUtils.isEmpty(environment) ? "prod" : environment;
    }
}
