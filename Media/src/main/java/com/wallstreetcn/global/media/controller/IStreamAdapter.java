package com.wallstreetcn.global.media.controller;

import android.view.View;

import tv.danmaku.ijk.media.widget.media.IMediaController;

/**
 * Created by zhangyang on 16/7/21.
 */
public interface IStreamAdapter extends IMediaController {
    void setLiveRoomCount(String count);

    void setIsLive(boolean isLive);

    void addActionBar(View view);
}
