package com.wallstreetcn.global.media.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.wallstreetcn.global.media.MEndTimeTextView;
import com.wallstreetcn.global.media.widget.WscnMediaView;
import com.wallstreetcn.helper.utils.TLog;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.wallstreetcn.global.media.R;
import com.wallstreetcn.global.media.WscnMediaEntity;
import com.wallstreetcn.global.media.WscnMediaManager;
import com.wallstreetcn.global.media.model.PlayUriEntity;
import com.wallstreetcn.global.media.model.PlayUrlUtils;
import com.wallstreetcn.global.media.utils.PlaySpeedUtils;
import com.wallstreetcn.global.media.utils.WscnMediaUtils;
import com.wallstreetcn.global.media.widget.PlayTitleLayout;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.data.CollectionUtil4Data;
import com.wallstreetcn.helper.utils.rx.LazyAction;
import com.wallstreetcn.helper.utils.rx.RxUtils;
import com.wallstreetcn.helper.utils.snack.MToastHelper;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import static tv.danmaku.ijk.media.player.IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START;

/**
 * Created by zhangyang on 16/4/6.
 */
public class BaseMediaController extends BaseTouchVideoController
        implements IStreamAdapter, IMediaPlayer.OnInfoListener {
    public static final String TAG = BaseMediaController.class.getSimpleName();
    private MediaController.MediaPlayerControl mPlayer;
    private boolean playable = true;

    private LiveSourceChangeCallback liveSourceChangeCallback;

    protected ImageView liveFullScreen;
    protected TextView liveRoomSource, tvNotice;
    protected ViewGroup layoutSource;
    public Config config = new Config();
    public WscnMediaEntity mediaEntity;
    public WscnMediaView mediaView;
    private Handler mHandler;

    private Method touchPointInViewMethod;
    private TextView live_speed;

    private String mKey = "";

    private PlayUrlChooserLayout chooser;

    private PlayTitleLayout titleLayout;

    private OnClickListener titleActionListener;

    StringBuilder curmFormatBuilder;
    Formatter curmFormatter;

    private final OnClickListener mPauseListener = v -> {
        try {
            responseToPauseBtn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public void responseToPauseBtn() {
        if (playable || mPlayer.isPlaying()) {
            play();
        }
    }

    public void play() {
        doPauseResume();
        show(5000);
    }

    public boolean controllerIsShow() {
        boolean flag = false;
        return flag;
    }

    public BaseMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public BaseMediaController(Context context) {
        super(context);
        setBackgroundColor(Color.parseColor("#11000000"));
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        IC_MEDIA_PAUSE_ID = R.drawable.short_video_pause;
        IC_MEDIA_PLAY_ID = R.drawable.short_video_play;
    }


    public void setLiveSourceChangeCallback(LiveSourceChangeCallback liveSourceChangeCallback) {
        this.liveSourceChangeCallback = liveSourceChangeCallback;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    private View actionBar;

    public void addActionBar(@Nullable View view) {
        this.actionBar = view;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            setClickable(false);
        } else {
            setClickable(true);
        }
    }

    @Override
    public void show(int timeout) {
        try {
            super.show(timeout);
            if (actionBar != null) {
                actionBar.setVisibility(View.VISIBLE);
            }

            mEndTime.setText(stringForTime(mPlayer.getDuration() - mPlayer.getCurrentPosition()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String stringForTime(int timeMs) {
        if (curmFormatBuilder == null) {
            curmFormatBuilder = new StringBuilder();
            curmFormatter = new Formatter(curmFormatBuilder, Locale.getDefault());
        }

        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        curmFormatBuilder.setLength(0);
        if (hours > 0) {
            return curmFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return curmFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    public void hide() {
        if (layoutSource != null) layoutSource.setVisibility(GONE);

        if (controllerIsShow()) {
            setVisibility(View.VISIBLE);

            if (actionBar != null)
                actionBar.setVisibility(View.VISIBLE);
            return;
        } else if (actionBar != null) {
            actionBar.setVisibility(View.GONE);
        }

        if (isPlaying())
            super.hide();
        else
            show();
    }


    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        super.setMediaPlayer(player);
        this.mPlayer = player;
        if (mEndTime instanceof MEndTimeTextView) {
            ((MEndTimeTextView) mEndTime).setMediaPlayer(player);
        }
        initSpeed();
        setSpeedVisible();
    }

    private static float lastSpeed = PlaySpeedUtils.s_speed;

    public static float getLastSpeed() {
        return lastSpeed;
    }

    private void setSpeedVisible() {
        if (live_speed != null) {
            live_speed.setVisibility(!config.isLive && config.isFullScreen && config.speedEnable
                    && WscnMediaManager.instance().enableSpeed() ? VISIBLE : GONE);
        }
    }

    private void initSpeed() {
        if (config.speedEnable) {
            if (live_speed != null && !config.isLive) {
                live_speed.setOnClickListener(v -> {
                    chooser.registerClickView(live_speed, false);
                    List<CharSequence> list = PlaySpeedUtils.getSpeedsValues();
                    int cur = PlaySpeedUtils.getIndexSpeed(lastSpeed);
                    chooser.setSpeedChange(list, cur, index -> {
                        float speed = PlaySpeedUtils.speeds[index];
                        layoutSource.setVisibility(GONE);
                        setSpeedValue(speed);
                    });
                    layoutSource.setVisibility(VISIBLE);
                });
                if (lastSpeed != PlaySpeedUtils.s_speed)
                    setSpeedValue(lastSpeed);
            }
        }
    }


    private void setSpeedValue(float speed) {
        if (!config.speedEnable) return;
        this.lastSpeed = speed;
        if (WscnMediaManager.instance().setSpeed(speed)) {
            if (live_speed != null)
                live_speed.setText(speed + "x");
        }
    }

    public void setPlayUrls(List<PlayUriEntity> playUrls, String resolution) {
        if (CollectionUtil4Data.isEmpty(playUrls))
            throw new IllegalArgumentException("playUrls can't be empty");
        mKey = PlayUrlUtils.getPlayUrlsKey(playUrls);
        if (TextUtils.isEmpty(resolution))
            resolution = PlayUriEntity.DEFAULT;
        config.putPlayUrls(playUrls, resolution);
    }

    @Override
    protected int getControllerLayoutId() {
        if (config.isFullScreen)
            return R.layout.live_room_view_live_controller;
        else return R.layout.live_room_view_video_controller_por;
    }


    public void setIsLive(boolean isLive) {
        config.isLive = false;
        super.setIsLive(isLive);
    }

    public boolean isLive() {
        if (config != null)
            return config.isLive;
        else
            return false;
    }

    protected OnClickListener fullScreenListener;

    public void setOnFullScreenListener(OnClickListener listener) {
        this.fullScreenListener = listener;
    }


    public void setConfig(Config config) {
        this.config = config;
    }

    public void setTitleActionListener(String title, OnClickListener listener) {
        this.titleActionListener = listener;
        this.config.videoTitle = title;
    }

    private View rootView;

    @Override
    protected void initControllerView(View v) {
        super.initControllerView(v);
        rootView = v;
        liveFullScreen = v.findViewById(R.id.live_fullscreen);
        tvNotice = v.findViewById(R.id.tv_notice);
        live_speed = v.findViewById(R.id.live_speed);
        initSourceControl(v);
        initTitleLayout(v);
        addListener();
        initConfig();
        initMHandler();
     /*   View controlRelativeLayout = v.findViewById(R.id.controlRelativeLayout);
        if(controlRelativeLayout != null && controlRelativeLayout instanceof ViewGroup){
            ViewGroup.LayoutParams params = controlRelativeLayout.getLayoutParams();
            params.height = ScreenUtils.dip2px(200f);
            controlRelativeLayout.setLayoutParams(params);
        }*/
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isClickInChild = isClickInChildView(event);
            rootViewVisible = (rootView.getVisibility() == View.VISIBLE);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if(isClickInChild && isCanTouch && rootView != null && rootViewVisible){
                mHandler.removeMessages(1);
                show(5000);
                rootView.setVisibility(View.VISIBLE);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    boolean isClickInChild = false;
    boolean rootViewVisible = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isClickInChild = isClickInChildView(event);
            rootViewVisible = (rootView.getVisibility() == View.VISIBLE);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if(isClickInChild && isCanTouch && rootView != null && rootViewVisible){
                mHandler.removeMessages(1);
                show(5000);
                rootView.setVisibility(View.VISIBLE);
            }
        }
        return result;
    }

    private boolean isClickInChildView(MotionEvent ev) {
        try {
            int childCount = getChildCount();
            boolean isClickInView = false;
            int actionIndex = ev.getActionIndex();
            final float x = ev.getX(actionIndex);
            final float y = ev.getY(actionIndex);
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (!isClickInView)
                    isClickInView = (boolean) touchPointInViewMethod.invoke(this, x, y, view, null);
            }
            return isClickInView;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initMHandler() {
        try {
            Class cls = getClass();
            while (cls.getSimpleName().contains("PlayerMediaController") == false) {
                cls = cls.getSuperclass();
            }
            Field field = cls.getDeclaredField("mHandler");
            if (field != null) {
                field.setAccessible(true);
                mHandler = (Handler) field.get(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Class cls = Class.forName("android.view.ViewGroup");
            touchPointInViewMethod = cls.getDeclaredMethod("isTransformedTouchPointInView", float.class, float.class, View.class, PointF.class);
            touchPointInViewMethod.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTitleLayout(View rootView) {
        titleLayout = rootView.findViewById(R.id.playTitleBar);
        if (titleLayout == null)
            return;
        if (chooser == null) {
            liveSourceChangeInit(rootView);
        }
    }

    private void initConfig() {
        onScreenOrientationChanged(!config.isFullScreen);
        if (titleLayout != null) {
            titleLayout.setConfig(config);
            titleLayout.setFav(config.isFavArticle);
        }
    }

    public void setFav(boolean isFav) {
        this.config.isFavArticle = isFav;
        if (titleLayout != null)
            titleLayout.setFav(config.isFavArticle);
    }

    private void addListener() {
        if (liveFullScreen != null)
            liveFullScreen.setOnClickListener(fullScreenListener);

        if (mPauseButton != null)
            mPauseButton.setOnClickListener(mPauseListener);

        if (titleLayout != null)
            titleLayout.setTitleAndOnClickListener(v -> {
                if (titleActionListener != null) {
                    titleActionListener.onClick(v);
                }
            });
    }

    private void initSourceControl(View rootView) {
        liveRoomSource = rootView.findViewById(R.id.live_room_source);
        if (!config.hasMultiUrls) return;
        liveSourceChangeInit(rootView);

        liveRoomSource.setOnClickListener(ls -> {
            if (chooser != null) {
                chooser.registerClickView(liveRoomSource, false);
                chooser.setModeDownload(false);
                chooser.setPlayUrl(config, entity -> {
                    layoutSource.setVisibility(GONE);
                    changeUrl(entity);
                });
                layoutSource.setVisibility(VISIBLE);
            }
        });
        liveRoomSource.setText(config.resolutionStr());
        liveRoomSource.setVisibility((config.isFullScreen && config.hasMultiUrls) ? VISIBLE : GONE);
        setSpeedVisible();
    }

    private void liveSourceChangeInit(View rootView) {
        chooser = rootView.findViewById(R.id.chooseLayout);
        layoutSource = chooser.findViewById(R.id.layout_source);
    }


    private void reconnect() {
        mPlayer.start();
    }


    public void changeUrl(PlayUriEntity entity) {
        if (entity == null) return;

        config.uriEntity = entity;
        liveRoomSource.setText(entity.getTypeString());
        if (liveSourceChangeCallback != null) {
            WscnMediaEntity media = WscnMediaEntity.buildEntityWithKey(entity.uri, mKey);
            WscnMediaUtils.setVideoPlayPosition(media, getCurrentPosition());
            liveSourceChangeCallback.change(entity);
        }
    }

    public void onScreenOrientationChanged(boolean isVertical) {
        config.isFullScreen = !isVertical;
        if (liveRoomSource != null) {
            liveRoomSource.setVisibility((config.isFullScreen && config.hasMultiUrls) ? VISIBLE : GONE);
        }
        setSpeedVisible();
        if (liveFullScreen != null) {
            liveFullScreen.setImageResource(isVertical ?
                    R.drawable.live_media_control_fullscreen : R.drawable.live_media_control_fullscreen);
        }
        if (titleLayout != null && titleActionListener != null) {
            titleLayout.setVisibility(config.isFullScreen ? VISIBLE : GONE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        hide();
        super.onDetachedFromWindow();
    }


    private void showNotice(String notice, boolean visible) {
        if (tvNotice == null) return;
        tvNotice.setText(notice);
        tvNotice.setVisibility(visible ? VISIBLE : GONE);
    }

    private boolean isBuffering = false;

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        TLog.d(TAG, "onInfo:IMediaPlayer-> what:" + what + "  -  extra:" + extra);
        if (config.isLive && (what == IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH || what == IMediaPlayer.MEDIA_INFO_BUFFERING_START)) {
            isBuffering = true;
            lazyAction.beep();
            showSpeed();
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END || what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START
                || what == MEDIA_INFO_AUDIO_RENDERING_START) {
            showNotice(null, false);
            isBuffering = false;
            dispose(speedSp);
        }
        return false;
    }

    public boolean checkUrlError() {
        if (config != null
                && config.hasMultiUrls
                && mPlayer != null
                && mPlayer.getCurrentPosition() == 0
                && config.uriEntity != null
                && !TextUtils.equals(config.uriEntity.resolution, PlayUriEntity.ORIGINAL)) {
            changeUrl(config.getPlayUriEntityByType(PlayUriEntity.ORIGINAL));
            return true;
        } else {
            return false;
        }
    }

    Disposable speedSp;

    private void showSpeed() {
        show();
        showNotice("", true);
        speedSp = RxUtils.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(Long::intValue)
                .subscribe(aLong -> {
                    StringBuilder sb = new StringBuilder("loading");
                    for (int i = 0; i <= aLong % 4; i++) {
                        sb.append(".");
                    }
                    showNotice(sb.toString(), true);
                });
    }


    LazyAction lazyAction = new LazyAction(6000, integer -> {
        if (isBuffering) {
            reconnect();
            MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.live_room_no_internet_change_dps));
        }
    });

    public void left() {
        dispose(speedSp);
        lazyAction.dispose();
    }


    private void dispose(Disposable sp) {
        RxUtils.dispose(sp);
    }
}
