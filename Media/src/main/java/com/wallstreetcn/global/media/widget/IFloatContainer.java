package com.wallstreetcn.global.media.widget;

/**
 * Created by chanlevel on 2017/3/7.
 */

public interface IFloatContainer extends IVideoContainer {

    void fly(AnimationListener listener);

    void land(AnimationListener listener);

    void fullScreen(AnimationListener listener);

    int state();

    void show(boolean show);

    interface AnimationListener {
        void finish();
    }


}
