package com.wallstreetcn.global.media.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;

import com.micker.helper.system.ScreenUtils;
import com.wallstreetcn.global.media.R;

import java.util.List;

public class MediaPlayChooser extends RadioGroup implements RadioGroup.OnCheckedChangeListener {

    public MediaPlayChooser(Context context) {
        super(context);
    }

    public MediaPlayChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private MediaChooseCallback listener;
    private List<CharSequence> chooses;
    private int current;

    public MediaPlayChooser setChooses(List<CharSequence> chooses, int current) {
        this.chooses = chooses;
        this.current = current;
        return this;
    }

    public void build() {
        removeAllViews();

        int index = 0;

        for (CharSequence choose : chooses) {
            if (getChildCount() >= chooses.size()) break;
            addRadioItem(choose, index);
            index++;
        }
//        this.check(current);
    }

    private RadioButton addRadioItem(CharSequence playUriEntity, int index) {
        Context context = getContext();
        RadioButton rdb = new RadioButton(context);
        rdb.setBackgroundResource(R.color.black);
        rdb.setButtonDrawable(null);
        rdb.setGravity(Gravity.CENTER);
        rdb.setPadding(0, ScreenUtils.dip2px(10), 0, ScreenUtils.dip2px(10));
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = ScreenUtils.dip2px(1);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        rdb.setLayoutParams(lp);
        rdb.setTextSize(14);
        rdb.setTextColor(ContextCompat.getColor(getContext(), current == index ? R.color.color_1482f0 : R.color.white));
        rdb.setText(playUriEntity);
        rdb.setId(index);
        addView(rdb, lp);
        return rdb;
    }


    public int getCurrent() {
        return current;
    }

    public void setListener(MediaChooseCallback listener) {
        this.listener = listener;
        if (listener == null) setOnCheckedChangeListener(null);
        else
            this.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        current = checkedId;
        if (listener != null) listener.change(checkedId);
    }

}
