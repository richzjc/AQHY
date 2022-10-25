package com.micker.five.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;

import com.micker.home.R;
import com.wallstreetcn.global.media.controller.BaseMediaController;

/**
 * Created by chanlevel on 2017/2/6.
 */

public class VideoMediaController extends BaseMediaController {

    public boolean fullScreenIconSwitch = true;

    public VideoMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public VideoMediaController(Context context) {
        super(context);
    }


    @Override
    protected int getControllerLayoutId() {
        return R.layout.live_room_view_video_controller_por;
    }


    @Override
    protected void initControllerView(View v) {
        super.initControllerView(v);
    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        super.setMediaPlayer(player);
    }

}
