package com.micker.global.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;
import com.micker.global.R;
import com.micker.helper.system.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wscn on 16/12/9.
 */

public class IndicatorView extends View implements NavigatorHelper.OnNavigatorScrollListener, IPagerNavigator {

    private int mMinWidth;
    private int mMaxWidth;
    private int mHeight;
    private int mNormalColor = Color.parseColor("#666666");
    private int mSelectedColor = Color.WHITE;
    private int mSpacing;
    private int mCount = 1;

    private NavigatorHelper helper;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Float> mWidthArray = new SparseArray<Float>();
    private SparseArray<Float> evel = new SparseArray<Float>();
    private List<RectF> mPoints = new ArrayList<>();
    private LinearInterpolator widthInterpolator = new LinearInterpolator();
    private PercentageCallback callback;


    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, int mMaxWidth, int mNormalColor, PercentageCallback callback) {
        super(context);
        init();
        this.mMaxWidth = mMaxWidth;
        this.mNormalColor = mNormalColor;
        this.callback = callback;
    }

    private void init() {
        mMinWidth = ScreenUtils.dip2px(5);
        mMaxWidth = ScreenUtils.dip2px(10);
        mHeight = ScreenUtils.dip2px(5);
        mSpacing = ScreenUtils.dip2px(5);
        helper = new NavigatorHelper();
        helper.setNavigatorScrollListener(this);
        helper.setSkimOver(true);
        mNormalColor = ContextCompat.getColor(getContext(), R.color.day_mode_color_e6e6e6);
        mSelectedColor = ContextCompat.getColor(getContext(), R.color.color_1482f0);

    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public void setNormalColor(int color) {
        this.mNormalColor = color;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY)
            return width;
        else
            return (mCount - 1) * mMinWidth + mMaxWidth
                    + (mCount - 1) * mSpacing
                    + getPaddingLeft() + getPaddingRight();
    }

    public int getWidgetWidth() {
        return (mCount - 1) * mMinWidth + mMaxWidth
                + (mCount - 1) * mSpacing
                + getPaddingLeft() + getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY)
            return height;
        else
            return mHeight + getPaddingTop() + getPaddingBottom();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        preparePoints();
    }

    private void preparePoints() {
        mPoints.clear();
        if (mCount > 0) {
            int xTop = getPaddingLeft();
            int yTop = getPaddingTop();
            int yBottom = yTop + mHeight;
            int xBottom = xTop + mMinWidth;
            for (int i = 0; i < mCount; i++) {
                RectF rect = new RectF(xTop, yTop, xBottom, yBottom);
                mPoints.add(rect);
                xTop = xBottom + mSpacing;
                xBottom = xTop + mMinWidth;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPoints == null || mPoints.isEmpty())
            return;
        float xtop = getPaddingLeft();
        float xBottom;
        for (int i = 0; i < mCount; i++) {
            float widht = mWidthArray.get(i, (float) mMinWidth);
            float value = evel.get(i, 0.0f);
            mPaint.setColor(ArgbEvaluatorHolder.eval(value, mNormalColor, mSelectedColor));
            xBottom = xtop + widht;
            RectF rect = mPoints.get(i);
            rect.set(xtop, rect.top, xBottom, rect.bottom);
            canvas.drawRect(rect, mPaint);
            xtop = xBottom + mSpacing;
        }
    }

    public void setCount(int count) {
        mCount = count;
        helper.setTotalCount(count);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        float value = widthInterpolator.getInterpolation(enterPercent);
        float width = mMinWidth + (mMaxWidth - mMinWidth) * value;
        mWidthArray.put(index % mCount, width);
        evel.put(index % mCount, value);
        if (callback != null) {
            callback.onEnter(index % mCount, value);
        }
        invalidate();
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        float value = widthInterpolator.getInterpolation(leavePercent);
        float radius = mMaxWidth + (mMinWidth - mMaxWidth) * value;
        mWidthArray.put(index % mCount, radius);
        evel.put(index % mCount, 1 - value);

        if (callback != null) {
            callback.onLeave(index % mCount, 1 - value);
        }
        invalidate();
    }

    @Override
    public void onSelected(int index, int totalCount) {
        mWidthArray.put(index % mCount, (float) mMaxWidth);
        evel.put(index % mCount, 1.0f);
        invalidate();
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        mWidthArray.put(index % mCount, (float) mMinWidth);
        evel.put(index % mCount, 0.0f);
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        helper.onPageScrolled(position % mCount, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        helper.onPageSelected(position % mCount);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        helper.onPageScrollStateChanged(state);
    }

    @Override
    public void onAttachToMagicIndicator() {

    }

    @Override
    public void onDetachFromMagicIndicator() {

    }

    public void setSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
    }

    @Override
    public void notifyDataSetChanged() {
        preparePoints();
        invalidate();
    }
}
