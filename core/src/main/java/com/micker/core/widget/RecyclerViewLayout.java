package com.micker.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by  Leif Zhang on 2018/3/16.
 * Email leifzhanggithub@gmail.com
 */

public class RecyclerViewLayout extends RelativeLayout {
    public RecyclerViewLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int[] attrs = new int[] { android.R.attr.selectableItemBackground };
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs);
//        setBackground(a.getDrawable(0));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}
