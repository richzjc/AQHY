package com.wallstreetcn.global.media.controller;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wallstreetcn.global.media.R;
import com.wallstreetcn.global.media.model.PlayUriEntity;
import com.wallstreetcn.helper.utils.router.RouterHelper;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayUrlChooserLayout extends RelativeLayout {

    public static String VIDEO_CACHE_ROUTER = "wscn://wallstreetcn.com/nativeapp/download";

    public PlayUrlChooserLayout(Context context) {
        super(context);
        init();
    }

    public PlayUrlChooserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayUrlChooserLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private View clickView;
    private Boolean isShowClickViewBottom;

    //    private PlayUrlChooser chooser;
    private MediaPlayChooser playChooser;
    private View viewCacheLayout;
    private View viewCache;
    private View viewCacheTip;
    private View layoutSource;


    public void registerClickView(View clickView, Boolean isShowClickViewBottom) {
        this.clickView = clickView;
        this.isShowClickViewBottom = isShowClickViewBottom;
    }

    private void relayoutLayoutSource() {
        if(clickView == null)
            return;
        int[] location = new int[2];
        clickView.getLocationInWindow(location);
        int centerClickViewX = location[0] + clickView.getPaddingLeft() + (clickView.getWidth() - clickView.getPaddingLeft() - clickView.getPaddingRight()) / 2;
        int layoutSourceWidth = layoutSource.getWidth();
        if (layoutSourceWidth > 0) {
            int startX = centerClickViewX - layoutSourceWidth / 2;
            int endX = startX + layoutSourceWidth;
            if (endX > getWidth()) {
                int gap = endX - getWidth();
                startX = startX - gap;
                endX = endX - gap;
            }

            if(isShowClickViewBottom){
                int margin = initChooserBottomMargin();
                layoutSource.layout(startX, margin, endX, margin + layoutSource.getHeight());
            }else{
                int margin = initChooserBottomMargin();
                layoutSource.layout(startX, getHeight() - margin - layoutSource.getHeight(), endX, getHeight() - margin);
            }
        }
    }

    private int initChooserBottomMargin() {
        ViewGroup group = null;
        if (clickView != null)
            group = (ViewGroup) clickView.getParent();

        if (group != null) {
            return group.getHeight() + ScreenUtils.dip2px(25f);
        }else{
            return 0;
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.global_view_play_chooser_layout, this, true);
        viewCacheTip = findViewById(R.id.viewCacheTip);
        viewCache = findViewById(R.id.viewCache);
        viewCacheLayout = findViewById(R.id.viewCacheLayout);
        playChooser = findViewById(R.id.rdg_speed);
        layoutSource = findViewById(R.id.layout_source);
        viewCache.setOnClickListener(v -> {
            setCacheRouter();
        });
        viewCacheTip.setVisibility(GONE);
        viewCacheLayout.setVisibility(VISIBLE);
    }

    public void setPlayUrl(Config config, PlayUrlChooser.PlayUrlSelectListener listener) {
        int cur = 0;
        List<CharSequence> list = new ArrayList<>(config.playUrls.size());
        for (int i = 0; i < config.playUrls.size(); i++) {
            if (config.resolution() == config.playUrls.get(i).resolution) {
                cur = i;
            }
            list.add(config.playUrls.get(i).getTypeSpannable());
        }

        playChooser.setChooses(list, cur).build();
        playChooser.setListener(index -> {
            if (listener != null) {
                listener.select(config.playUrls.get(index));
            }
        });
        viewCacheTip.setVisibility(GONE);
        for (PlayUriEntity entity : config.playUrls) {
            if (entity.cached) {
                viewCacheTip.setVisibility(VISIBLE);
            }
        }
    }

    public void setSpeedChange(List<CharSequence> chooses, int index, MediaChooseCallback change) {
        viewCacheLayout.setVisibility(GONE);
        playChooser.setChooses(chooses, index).build();
        playChooser.setListener(change);
        ViewGroup.LayoutParams params = layoutSource.getLayoutParams();
        if (params != null) {
            params.width = ScreenUtils.dip2px(70);
            layoutSource.setLayoutParams(params);
        }
    }

    private void setCacheRouter() {
        Bundle bundle = new Bundle();
        bundle.putInt("index", 1);
        RouterHelper.open(VIDEO_CACHE_ROUTER, getContext(), bundle);
    }

    public void setModeDownload(boolean download) {
        if (download) {
            viewCacheLayout.setVisibility(VISIBLE);
            ViewGroup.LayoutParams params = layoutSource.getLayoutParams();
            if (params != null) {
                params.width = ScreenUtils.dip2px(150);
                layoutSource.setLayoutParams(params);
            }
        } else {
            viewCacheLayout.setVisibility(GONE);
            ViewGroup.LayoutParams params = layoutSource.getLayoutParams();
            if (params != null) {
                params.width = ScreenUtils.dip2px(70);
                layoutSource.setLayoutParams(params);
            }
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        relayoutLayoutSource();
    }
}
