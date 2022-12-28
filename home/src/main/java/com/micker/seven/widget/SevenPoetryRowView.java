package com.micker.seven.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.micker.first.widget.TianTextView;
import com.micker.helper.ResourceUtils;
import com.micker.helper.system.ScreenUtils;
import com.micker.home.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SevenPoetryRowView extends FrameLayout {

    public String bindData;

    public SevenPoetryRowView(@NonNull Context context) {
        super(context);
    }

    public SevenPoetryRowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SevenPoetryRowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindData(String poetry) {
        this.bindData = poetry;
        int childCount = getChildCount();
        List<TianTextView> list = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            list.add((TianTextView) getChildAt(i));
        }
        removeAllViews();
        int size = poetry.length();
        if (childCount < size) {
            for (int i = childCount; i < size; i++) {
                list.add(new TianTextView(getContext()));
            }
        }

        if (list.size() > size) {
            list = list.subList(0, size);
        }

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setText(String.valueOf(poetry.charAt(i)));
            list.get(i).setTextColor(ResourceUtils.getColor(R.color.day_mode_text_color1_333333));
            list.get(i).setGravity(Gravity.CENTER);
            list.get(i).setVisibility(View.VISIBLE);
            addView(list.get(i));
        }
    }

    public void bindDataTestBottom(String poetry) {
        this.bindData = poetry;
        int childCount = getChildCount();
        List<TianTextView> list = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            list.add((TianTextView) getChildAt(i));
        }
        removeAllViews();
        String splitStr = "。，？；！、";
        List<String> charList = new ArrayList<>();
        int poetryLenth = poetry.length();
        for (int i = 0; i < poetryLenth; i++) {
            String value = String.valueOf(poetry.charAt(i));
            if (!splitStr.contains(value)) {
                charList.add(value);
            }
        }

        int size = charList.size();
        if (childCount < size) {
            for (int i = childCount; i < size; i++) {
                list.add(new TianTextView(getContext()));
            }
        }

        if (list.size() > size) {
            list = list.subList(0, size);
        }


        for (int i = 0; i < list.size(); i++) {
            int index = new Random().nextInt(charList.size());
            String value = charList.get(index);
            charList.remove(value);
            list.get(i).setText(value);
            list.get(i).setVisibility(View.VISIBLE);
            list.get(i).setTextColor(ResourceUtils.getColor(R.color.day_mode_text_color1_333333));
            list.get(i).setGravity(Gravity.CENTER);
            addView(list.get(i));
        }
    }

    public void bindDataTest(String poetry) {
        this.bindData = poetry;
        int childCount = getChildCount();
        List<TianTextView> list = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            list.add((TianTextView) getChildAt(i));
        }
        removeAllViews();
        int size = poetry.length();
        if (childCount < size) {
            for (int i = childCount; i < size; i++) {
                list.add(new TianTextView(getContext()));
            }
        }

        if (list.size() > size) {
            list = list.subList(0, size);
        }

        String splitStr = "。，？；！、";
        for (int i = 0; i < list.size(); i++) {
            String value = String.valueOf(poetry.charAt(i));
            if (splitStr.contains(value)) {
                list.get(i).setText(value);
            } else {
                list.get(i).setTag(value);
            }
            list.get(i).setVisibility(View.VISIBLE);
            list.get(i).setTextColor(ResourceUtils.getColor(R.color.day_mode_text_color1_333333));
            list.get(i).setGravity(Gravity.CENTER);
            addView(list.get(i));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int gap = ScreenUtils.dip2px(5f);
        int itemWidth = (widthsize - gap * 7) / 8;
        int rowGap = ScreenUtils.dip2px(10f);
        double rowCount = Math.ceil(getChildCount() * 1.0 / 8);
        double totalHeight = rowCount * itemWidth + (rowCount - 1) * rowGap;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int spec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
            TianTextView textView = (TianTextView) getChildAt(i);
            textView.measure(spec, spec);
        }
        setMeasuredDimension(widthsize, (int) totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int rowGap = ScreenUtils.dip2px(10f);
        int gap = ScreenUtils.dip2px(5f);
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int row = (int) Math.floor(i * 1.0 / 8);
            int index = (i % 8);
            int startX = index * view.getMeasuredWidth() + index * gap;
            int startY = row * view.getMeasuredHeight() + row * rowGap;
            view.layout(startX, startY, startX + view.getMeasuredWidth(), startY + view.getMeasuredHeight());
        }
    }
}
