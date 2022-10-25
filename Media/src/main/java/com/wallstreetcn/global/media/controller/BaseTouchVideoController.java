package com.wallstreetcn.global.media.controller;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;

import tv.danmaku.ijk.media.widget.media.AndroidMediaController;

/**
 * Created by  Leif Zhang on 2017/12/18.
 * Email leifzhanggithub@gmail.com
 */

public class BaseTouchVideoController extends AndroidMediaController
        implements MediaController.MediaPlayerControl {


    private MediaController.MediaPlayerControl mPlayer;
    private TouchEventController touchEventController;
    protected boolean isCanTouch = true;

    public void setCanTouch(boolean canTouch) {
        isCanTouch = canTouch;
    }

    public BaseTouchVideoController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTouchVideoController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public BaseTouchVideoController(Context context) {
        super(context);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        touchEventController = new TouchEventController(this, this);
    }

    public void setIsLive(boolean isLive){
        if(touchEventController != null)
            touchEventController.setIsLive(isLive);
    }

    @Override
    protected void initControllerView(View v) {
        super.initControllerView(v);
        touchEventController.initView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCanTouch) {
            touchEventController.onTouchEvent(event);
        }
        return isCanTouch;
    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        super.setMediaPlayer(player);
        this.mPlayer = player;
    }

    @Override
    public boolean isPlaying() {
        if(mPlayer == null) return false;
        return mPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return mPlayer.getBufferPercentage();
    }

    @Override
    public boolean canPause() {
        return mPlayer.canPause();
    }

    @Override
    public boolean canSeekBackward() {
        return mPlayer.canSeekBackward();
    }

    @Override
    public boolean canSeekForward() {
        return mPlayer.canSeekForward();
    }

    @Override
    public int getAudioSessionId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return mPlayer.getAudioSessionId();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mPlayer.seekTo(pos);
    }

    @Override
    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    @Override
    public void start() {
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }
}
