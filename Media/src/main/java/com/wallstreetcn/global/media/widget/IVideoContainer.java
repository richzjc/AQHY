package com.wallstreetcn.global.media.widget;

import android.view.ViewGroup;

import com.wallstreetcn.global.media.widget.WscnMediaView;

/**
 * Created by chanlevel on 2017/2/16.
 */

public interface IVideoContainer {

    void join(WscnMediaView videoView);


    void leave();

    ViewGroup videoHolder();

    void over();

}
