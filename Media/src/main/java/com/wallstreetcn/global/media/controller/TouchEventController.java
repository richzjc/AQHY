package com.wallstreetcn.global.media.controller;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.provider.Settings;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.micker.helper.TLog;
import com.wallstreetcn.global.media.R;
import com.wallstreetcn.global.media.widget.MediaControlProgressBar;

import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.widget.media.AndroidMediaController;

/**
 * Created by  Leif Zhang on 2017/12/11.
 * Email leifzhanggithub@gmail.com
 */

public class TouchEventController {

    private final String TAG = "TouchEventController";
    private int mPagingTouchSlop;
    private float downX, downY;
    private boolean isOnMove = false;
    private boolean isHorizontal, isVertical;
    private AndroidMediaController view;
    private MediaController.MediaPlayerControl control;
    private int middleX;
    private final int minBright = 20;
    private int maxVolume;
    private int currentVolume;
    private int curBright = 0;
    private AudioManager mAudioManager;
    private int curPlayerDistance;
    private TextView tv;
    private boolean isLive = false; //标记是否是直播的视频

    private MediaControlProgressBar audioBar, brightBar;

    public TouchEventController(AndroidMediaController view, MediaController.MediaPlayerControl control) {
        this.view = view;
        this.control = control;
        initData();
    }

    public void initView(View v) {
        audioBar = new MediaControlProgressBar(v.getContext());
        audioBar.initBar(R.drawable.lower_audio, R.drawable.raise_audio, maxVolume);
        audioBar.setProgress(currentVolume);
        brightBar = new MediaControlProgressBar(v.getContext());
        brightBar.initBar(R.drawable.lower_bright, R.drawable.raise_bright, 255 - minBright);
        brightBar.setProgress(curBright - minBright);
        brightBar.setVisibility(View.GONE);
        audioBar.setVisibility(View.GONE);
        addView(v, audioBar.getView());
        addView(v, brightBar.getView());
        initTextView(v);
    }

    public void setIsLive(boolean isLive){
        this.isLive = isLive;
    }

    private void initTextView(View root) {
        tv = new TextView(root.getContext());
        tv.setTextSize(17);
        tv.setPadding(30, 10, 30, 10);
        tv.setBackgroundColor(Color.parseColor("#88000000"));
        tv.setTextColor(Color.WHITE);
        tv.setVisibility(View.GONE);
        addView(root, tv);
    }

    private void addView(View root, View child) {
        if (root instanceof FrameLayout) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            ((FrameLayout) root).addView(child, lp);
        } else if (root instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            ((RelativeLayout) root).addView(child, lp);
        }
    }


    private void initData() {
        final ViewConfiguration conf = ViewConfiguration.get(view.getContext());
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
        curBright = getBrightness((Activity) view.getContext());//获取系统最大音量
        mAudioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取设备当前音量
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        TLog.i(TAG, "bright:" + curBright);
    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                setupConfig();
                break;
            case MotionEvent.ACTION_MOVE:
                if (checkIsOnMove(event)) {
                    if (view.isShowing()) {
                        view.hide();
                    }
                    onTouchMove(event);
                } else {
                    return;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isOnMove) {
                    if (!view.isShowing()) {
                        view.show();
                    } else {
                        view.hide();
                    }
                }
                resetConfig();
                break;
        }
    }

    private boolean checkIsOnMove(MotionEvent event) {
        if (isOnMove) {
            return true;
        } else {
            if (Math.abs(event.getX() - downX) > mPagingTouchSlop) {
                isOnMove = true;
                isVertical = true;
                return true;
            }
            if (Math.abs(event.getY() - downY) > mPagingTouchSlop) {
                isHorizontal = true;
                isOnMove = true;
                return true;
            }
        }
        return isOnMove;
    }

    private void setupConfig() {
        if (view.getMeasuredWidth() > 0) {
            int width = view.getMeasuredWidth();
            middleX = width / 2;

        }
        isHorizontal = false;
        isVertical = false;
        isOnMove = false;
        curPlayerDistance = -1;
        view.getParent().requestDisallowInterceptTouchEvent(true);

    }

    private void resetConfig() {
        view.getParent().requestDisallowInterceptTouchEvent(false);
        if (curPlayerDistance != -1) {
            tv.setText("");
            tv.setVisibility(View.GONE);
            control.seekTo(curPlayerDistance);
            control.start();
        }
        brightBar.setVisibility(View.GONE);
        audioBar.setVisibility(View.GONE);
    }

    private void onTouchMove(MotionEvent event) {
        if (isHorizontal) {
            float moveYDistance = (event.getY() - downY) / 3;
            if (downX > middleX) {
                if (Math.abs(moveYDistance) > mPagingTouchSlop) {
                    TLog.i(TAG, "Left:" + moveYDistance);
                    if (moveYDistance < 0) {
                        currentVolume++;
                    } else {
                        currentVolume--;
                    }
                    downY = event.getY();
                }
                currentVolume = currentVolume > maxVolume ? maxVolume : currentVolume;
                currentVolume = currentVolume < 0 ? 0 : currentVolume;
                adjustMusic();
                audioBar.setProgress(currentVolume);
                audioBar.setVisibility(View.VISIBLE);
            } else {
                TLog.i(TAG, "right:");
                curBright = (int) (curBright - moveYDistance / 2);
                curBright = curBright < minBright ? minBright : curBright;
                curBright = curBright > 255 ? 255 : curBright;
                setBrightness(curBright);
                brightBar.setProgress(curBright);
                brightBar.setVisibility(View.VISIBLE);
            }
        }
        if (isVertical && !isLive) {
            if (control.getDuration() == 0) {
                return;
            }
            tv.setVisibility(View.VISIBLE);
            float moveXDistance = (event.getX() - downX) / 3;
                if (moveXDistance > 0) {
                    doWithPlayer(1000);
                } else {
                    doWithPlayer(-1000);
                }

            String seekTime = stringForTime(curPlayerDistance);

            showSeekTime(timeDecorate(seekTime, stringForTime(control.getDuration())));
            downX = event.getX();
        }
    }

    private void doWithPlayer(float distance) {
        if (control.isPlaying()) {
            control.pause();
            curPlayerDistance = control.getCurrentPosition();
        }
        curPlayerDistance += distance;
        curPlayerDistance = curPlayerDistance < 0 ? 0 : curPlayerDistance;
        curPlayerDistance = curPlayerDistance > control.getDuration() ? control.getDuration() : curPlayerDistance;
        TLog.i(TAG, "distance:" + curPlayerDistance);

    }


    private void setBrightness(int curBright) {
        Activity activity = (Activity) view.getContext();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        float screenBright = (curBright <= 0 ? -1.0f : curBright / 255f);
        screenBright = screenBright > 1 ? 1 : screenBright;
        lp.screenBrightness = screenBright;
        activity.getWindow().setAttributes(lp);
    }

    private int getBrightness(Activity activity) {
        int brightValue = 0;
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            brightValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightValue;
    }


    private void adjustMusic() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume,
                AudioManager.FLAG_VIBRATE);
    }


    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void showSeekTime(CharSequence progress) {
        tv.setVisibility(View.VISIBLE);
        tv.setText(progress);
        TLog.i("progress", progress.toString());
    }


    private SpannableString timeDecorate(String seek, String total) {
        if (TextUtils.isEmpty(seek) || TextUtils.isEmpty(total)) {
            return new SpannableString(seek + " / " + total);
        }
        SpannableString sp = new SpannableString(seek + " / " + total);
        ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(view.getContext(),
                R.color.color_1482f0));
        sp.setSpan(span, 0, seek.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }
}
