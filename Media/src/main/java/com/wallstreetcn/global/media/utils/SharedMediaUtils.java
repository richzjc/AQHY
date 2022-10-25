package com.wallstreetcn.global.media.utils;

import com.wallstreetcn.helper.utils.SharedPrefsUtil;

public class SharedMediaUtils {

    public static boolean isAllowNoWifiPlay() {
        return true;
    }

    public static void setAllowNoWifiPlay(boolean allow) {
        SharedPrefsUtil.save("config", "allowNoWifiPlay", allow);
    }
}
