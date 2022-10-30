package com.micker.five.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.micker.helper.TLog;

public class ChartScrollView extends ScrollView {
    public ChartScrollView(Context context) {
        super(context);
    }

    public ChartScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChartScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private float downX = 0;
    private float downY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
            downY = ev.getY();
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if(getScrollY() == 0 && (ev.getY()  > downY))
                return super.onInterceptTouchEvent(ev);

            if(getChildCount() == 1 && (getScrollY() + getMeasuredHeight()) >= getChildAt(0).getMeasuredHeight() && ev.getY() < downY)
                return super.onInterceptTouchEvent(ev);

            requestDisallowInterceptTouchEvent(true);
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        TLog.e("@@@", "heightSize = " + heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (getChildCount() > 0) {
            View child0 = getChildAt(0);
            int height = getMeasuredHeight();
            int childHeight = child0.getMeasuredHeight();
            if (childHeight < height) {
                child0.layout(0, (height - childHeight) / 2, child0.getMeasuredWidth(), (height - childHeight) / 2 + child0.getMeasuredHeight());
            }
        }
    }
}
