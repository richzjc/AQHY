package com.wallstreetcn.global.media;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.wallstreetcn.global.media.controller.BaseMediaController;
import com.wallstreetcn.global.media.widget.VideoWidget;
import com.wallstreetcn.global.media.widget.WscnMediaView;
import com.wallstreetcn.helper.utils.SmartFloatUtil;
import com.wallstreetcn.helper.utils.router.RouterHelper;

import androidx.annotation.Nullable;

/**
 * Created by  Leif Zhang on 2017/12/22.
 * Email leifzhanggithub@gmail.com
 */

public class BaseVideoService extends Service {
    private static       boolean         isPlaying;
    private static       String          sRouterUrl;
    private static       WscnMediaEntity sMediaEntity;
    private static final String          DISMISS = "dismiss";

    private static Intent newIntent(Context context) {
        return newIntent(context, null);
    }

    private static Intent newIntent(Context context, String flag) {
        Intent intent = new Intent(context, BaseVideoService.class);
        if (!TextUtils.isEmpty(flag)) {
            intent.putExtra("flag", flag);
        }
        return intent;

    }

    public static void intentToStart(Context context, WscnMediaEntity entity, String routerUrl) {
        sMediaEntity = entity;
        sRouterUrl = routerUrl;
        context.startService(newIntent(context));
    }

    public static void intentToStop(Context context) {
        context.stopService(newIntent(context));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    public static boolean isPlayingCur(String routerUrl) {
        return isPlaying() && TextUtils.equals(routerUrl, sRouterUrl);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String flag = "";
        if (intent != null)
            flag = intent.getStringExtra("flag");
        if (TextUtils.equals(flag, DISMISS)) {
            dismissWidget();
        } else {
            setUpVideoView();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private VideoWidget   videoWidget;
    private WscnMediaView videoView;
    private String        TAG = "BaseVideoService";

    private void setUpVideoView() {
        if (sMediaEntity == null) {
            stopMediaPlay();
            return;
        }
        if (TextUtils.isEmpty(sMediaEntity.getUrl())) {
            stopMediaPlay();
            return;
        }
        if (!SmartFloatUtil.isFloatWindowOpAllowed()) {
            stopMediaPlay();
            return;
        }
        prepareWidget();
        if (videoView == null) {
            videoView = new WscnMediaView(this);
            videoView.setKeepScreenOn(true);
            ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            videoView.setOnCompletionListener(iMediaPlayer -> {
                stopMediaPlay();
            });
            if (videoWidget.getVideoHolder() != null)
                videoWidget.getVideoHolder().addView(videoView, vlp);
            videoView.setOnDropListener(() -> {
                stopMediaPlay();
            });
            videoView.setOnPreparedListener(iMediaPlayer -> {
                WscnMediaManager.instance().setSpeed(BaseMediaController.getLastSpeed());
                if (videoView != null)
                    videoView.start();
            });
        }
        sMediaEntity.setTag(TAG);
        videoView.setMediaEntity(sMediaEntity);
        videoView.setNeedWifiTips(false);
        videoWidget.land(null);
        videoWidget.join(videoView);
        videoWidget.show(true);
        videoView.start();
        isPlaying = true;
        WscnMediaManager.instance().setSpeed(BaseMediaController.getLastSpeed());
    }

    private void stopMediaPlay() {
        dismissAll();
        stopSelf();
    }

    public void dismissAll() {
        if (videoView != null) {
            try {
                videoView.stopPlayback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            videoView = null;
        }
        if (videoWidget != null) {
            videoWidget.dismiss();
            videoWidget = null;
        }
        sMediaEntity = null;
    }

    public void dismissWidget() {
        if (videoView != null)
            videoView.pause();
        if (videoWidget != null) {
            videoWidget.dismiss();
        }
    }


    private void prepareWidget() {
        try {
            if (videoWidget == null) {
                videoWidget = new VideoWidget(getApplication());
                videoWidget.init();
                videoWidget.setFloatClickListener(new VideoWidget.onFloatVideoClickListener() {

                    @Override
                    public void onClickVideo() {
                        if (TextUtils.isEmpty(sRouterUrl)) {
                            return;
                        }
                        dismissAll();
                        Bundle bundle = new Bundle();
                        bundle.putString("from", "FloatVideo");
                        RouterHelper.open(sRouterUrl, BaseVideoService.this, bundle);
                    }

                    @Override
                    public void onClickClose() {
                        stopMediaPlay();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        isPlaying = false;
        dismissAll();
        super.onDestroy();
    }
}
