package com.micker.core.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class IOSLoadingView extends View {
    private static final String TAG = IOSLoadingView.class.getSimpleName();
    /**
     * view宽度
     */
    private int width;
    /**
     * view高度
     */
    private int height;
    /**
     * 菊花的矩形的宽
     */
    private int widthRect;
    /**
     * 菊花的矩形的宽
     */
    private int heigheRect;
    /**
     * 菊花绘制画笔
     */
    private Paint rectPaint;
    /**
     * 循环绘制位置
     */
    private int pos = 0;
    /**
     * 菊花矩形
     */
    private Rect rect;
    /**
     * 循环颜色
     */
    private String[] color = {"#bbbbbb", "#aaaaaa", "#999999", "#888888", "#777777", "#666666",};

    public IOSLoadingView(Context context) {
        this(context, null);
    }

    public IOSLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IOSLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //根据个人习惯设置  这里设置  如果是wrap_content  则设置为宽高200
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            width = 60;
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = Math.min(width, height);
        }

        widthRect = width / 12;   //菊花矩形的宽
        heigheRect = 4 * widthRect;  //菊花矩形的高
        setMeasuredDimension(width, width);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE)
            invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制部分是关键了，菊花花瓣矩形有12个，我们不可能去一个一个的算出所有的矩形坐标，我们可以考虑
        //旋转下面的画布canvas来实现绘制，每次旋转30度
        //首先定义一个矩形
        if (getVisibility() == View.VISIBLE) {
            if (rect == null) {
                rect = new Rect((width - widthRect) / 2, 0, (width + widthRect) / 2, heigheRect);
            }
            for (int i = 0; i < 12; i++) {
                if (i - pos >= 5) {
                    rectPaint.setColor(Color.parseColor(color[5]));
                } else if (i - pos >= 0 && i - pos < 5) {
                    rectPaint.setColor(Color.parseColor(color[i - pos]));
                } else if (i - pos >= -7 && i - pos < 0) {
                    rectPaint.setColor(Color.parseColor(color[5]));
                } else if (i - pos >= -11 && i - pos < -7) {
                    rectPaint.setColor(Color.parseColor(color[12 + i - pos]));
                }

                canvas.drawRect(rect, rectPaint);  //绘制
                canvas.rotate(30, width / 2, width / 2);    //旋转
            }

            pos++;
            if (pos > 11) {
                pos = 0;
            }

            postInvalidateDelayed(100);  //一个周期用时1200
        }
    }
}
