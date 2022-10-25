package com.wallstreetcn.global.media.controller;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wallstreetcn.global.media.R;
import com.wallstreetcn.global.media.WscnMediaEntity;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

import com.wallstreetcn.global.media.model.PlayUriEntity;

import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * Created by chanlevel on 2017/2/8.
 */

public class PlayUrlChooser extends RadioGroup implements RadioGroup.OnCheckedChangeListener {

    public PlayUrlChooser(Context context) {
        super(context);
    }

    public PlayUrlChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private List<PlayUriEntity> playUrls;
    private String type = PlayUriEntity.DEFAULT;
    private PlayUriEntity current;

    public PlayUrlChooser putPlayUrls(List<PlayUriEntity> playUrls) {
        this.playUrls = playUrls;
        return this;
    }

    public PlayUrlChooser setDefaultResolution(String type) {
        this.type = type;
        return this;
    }

    public void build() {
        removeAllViews();
        int i = 0;
        for (; i < playUrls.size(); i++) {
            if (TextUtils.equals(type, playUrls.get(i).resolution)) {
                break;
            }
        }
        current = playUrls.get(i);
        type = current.resolution;

        int id = 0;
        for (PlayUriEntity entity : playUrls) {
            if (getChildCount() >= playUrls.size()) break;
            addRadioItem(entity, id);
            id++;
        }
        this.check(i);
//        setBackgroundResource(R.drawable.global_resolution_bg);
//        ViewGroup.LayoutParams params = getLayoutParams();
//        params.height = ScreenUtils.dip2px(41 * id);
//        setLayoutParams(params);
    }

    private RadioButton addRadioItem(PlayUriEntity playUriEntity, int id) {
        Context context = getContext();
        RadioButton rdb = new RadioButton(context);
        rdb.setBackgroundResource(R.color.day_mode_color_19000000);
        rdb.setButtonDrawable(null);
        rdb.setGravity(Gravity.CENTER);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(55));
        lp.topMargin = ScreenUtils.dip2px(1);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        rdb.setLayoutParams(lp);
//        String text = current == playUriEntity ? "√ " + playUriEntity.getTypeString() + "  " : playUriEntity.getTypeString();
        rdb.setTextSize(14);
        rdb.setTextColor(ContextCompat.getColor(getContext(), current == playUriEntity ? R.color.day_mode_theme_color_1478f0 : R.color.white ));
        rdb.setText(playUriEntity.getTypeSpannable());
        rdb.setId(id);
        addView(rdb, lp);
        return rdb;
    }


    public String getResolution() {
        return type;
    }

    public PlayUriEntity getCurrent() {
        return current;
    }

    PlayUrlSelectListener listener;

    public void setListener(PlayUrlSelectListener listener) {
        this.listener = listener;
        if (listener == null) setOnCheckedChangeListener(null);
        else
            this.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        PlayUriEntity entity = playUrls.get(checkedId);
        current = entity;
        type = entity.resolution;
        if (listener != null) listener.select(entity);
    }

    public interface PlayUrlSelectListener {
        void select(PlayUriEntity entity);
    }


}
