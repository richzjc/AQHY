package com.micker.core.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.micker.core.R;
import com.micker.helper.system.ScreenUtils;

/**
 * Created by micker on 16/6/17.
 */
public class CustomToolBar extends Toolbar {
    private MarqueeTextView mTitleTextView;

    private CharSequence mTitleText;

    private int mTitleTextColor;
    private int mTitleTextAppearance;


    private boolean isMarquee = true;

    public CustomToolBar(Context context) {
        super(context);
        resolveAttribute(context, null, R.attr.toolbarStyle);
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        resolveAttribute(context, attrs, R.attr.toolbarStyle);
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttribute(context, attrs, defStyleAttr);
    }

    private void resolveAttribute(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        // Need to use getContext() here so that we use the themed context
        context = getContext();
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.Toolbar, defStyleAttr, 0);
        final int titleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        if (titleTextAppearance != 0) {
            setTitleTextAppearance(context, titleTextAppearance);
        }
        String title = a.getString(R.styleable.Toolbar_title);

        int defaultColor = ContextCompat.getColor(getContext(), R.color.day_mode_text_color);
        mTitleTextColor = a.getColor(R.styleable.Toolbar_titleTextColor, defaultColor);
        if (mTitleTextColor != 0) {
            setTitleTextColor(mTitleTextColor);
        }
        setTitle(title);
        a.recycle();
    }

    public boolean isMarquee() {
        return isMarquee;
    }

    public void setMarquee(boolean marquee) {
        isMarquee = marquee;
    }

    @Override
    public CharSequence getTitle() {
        return mTitleText;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(mTitleText) && TextUtils.equals(title, getResources().getString(R.string.app_name))) {
            return;
        }
        if (!TextUtils.isEmpty(title)) {
            if (mTitleTextView == null) { // 懒加载
                final Context context = getContext();
                mTitleTextView = new MarqueeTextView(context, isMarquee);
                if (mTitleTextAppearance != 0) {
                    mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
                }
                mTitleTextView.setTextSize(18);
                if (mTitleTextColor != 0) {
                    mTitleTextView.setTextColor(mTitleTextColor);
                }
            }
            if (mTitleTextView.getParent() != this) {
                addCenterView(mTitleTextView);
            }
        } else if (mTitleTextView != null && mTitleTextView.getParent() == this) { // 当title为空时，remove
            removeView(mTitleTextView);
        } else {
            super.setTitle("");
        }
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
        mTitleText = title;
    }

    public void setTitleVisibility(int visibility) {
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(visibility);
        }
    }

    private void addCenterView(View v) {
        final ViewGroup.LayoutParams vlp = v.getLayoutParams();
        final LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }
        int marginLeft = ScreenUtils.dip2px(5);
        lp.setMargins(marginLeft, 0, marginLeft, 0);
        lp.gravity = Gravity.CENTER;
        addView(v, lp);
    }

    @Override
    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        mTitleTextAppearance = resId;
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    public void setTitleFocus(boolean isFocus) {
        if (mTitleTextView != null)
            mTitleTextView.setFocusable(false);
    }

    public void setTitleTextAlpha(float alpha) {
        if (mTitleTextView != null)
            mTitleTextView.setAlpha(alpha);
    }
}

