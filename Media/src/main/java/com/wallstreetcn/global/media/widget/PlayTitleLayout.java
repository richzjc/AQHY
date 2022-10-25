package com.wallstreetcn.global.media.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wallstreetcn.baseui.customView.IconView;

import com.wallstreetcn.global.media.R;
import com.wallstreetcn.global.media.controller.Config;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.Util;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class PlayTitleLayout extends LinearLayout implements View.OnClickListener {

    public PlayTitleLayout(Context context) {
        super(context);
        init();
    }

    public PlayTitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayTitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView titleTv;
    private IconView videoFav;
    private View     videoShare;
    private View     mainView;

    private Config config;

    private OnClickListener listener;
    private boolean         layoutVisible;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.global_view_play_title_bar, this);
        findViewById(R.id.videoBack).setOnClickListener(this);
        videoFav = findViewById(R.id.videoFav);
        videoFav.setOnClickListener(this);
        videoShare = findViewById(R.id.videoShare);
        videoShare.setOnClickListener(this);
        titleTv = findViewById(R.id.videoTitle);
        mainView = findViewById(R.id.mainView);
        mainView.setPadding(0, Util.getStatusBarHeight(getContext()), 0, 0);
        super.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onClick(v);
    }

    public void setConfig(Config config) {
        this.config = config;
        if (config != null) {
            setTitleTv(config.videoTitle);
            setVideoFavVisible(config.favVisible);
            setVideoShareVisible(config.shareVisible);
        }
    }

    public void setTitleAndOnClickListener(OnClickListener l) {
        layoutVisible = true;
        this.listener = l;
    }

    private void setTitleTv(String title) {
        titleTv.setText(title);
    }

    public void setFav(boolean fav) {
        videoFav.setText(ResourceUtils.getResStringFromId(fav ? R.string.icon_star : R.string.icon_not_star));
        videoFav.setTextColor(ContextCompat.getColor(getContext(), fav ? R.color.day_mode_theme_color_1478f0 : R.color.white));
    }

    @Override
    public void setVisibility(int visibility) {
        if (layoutVisible)
            super.setVisibility(visibility);
    }

    private void setVideoFavVisible(boolean visible) {
        videoFav.setVisibility(visible ? VISIBLE : GONE);
    }

    private void setVideoShareVisible(boolean visible) {
        videoShare.setVisibility(visible ? VISIBLE : GONE);
    }
}
