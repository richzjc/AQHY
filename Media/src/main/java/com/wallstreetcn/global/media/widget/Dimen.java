package com.wallstreetcn.global.media.widget;


import com.micker.helper.system.ScreenUtils;

/**
 * Created by chanlevel on 2017/3/23.
 */

public class Dimen {

    public static int VIDEO_DEFAULT_HEIGHT = 210;

    static {
        VIDEO_DEFAULT_HEIGHT = 9 * ScreenUtils.getScreenWidth() / 16;
    }

}
