package com.micker.global.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.micker.global.R;

/**
 * Created by wscn on 17/5/21.
 */

public class VirtualLineView extends View {

    boolean isHorizontal = true;
    int color = Color.TRANSPARENT;
    float dashGap = 0;
    float dashWidth = 0;

    Paint mPaint = new Paint();
    Path mPath = new Path();

    public VirtualLineView(Context context) {
        this(context, null);
    }

    public VirtualLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirtualLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.virtualLine);
        isHorizontal = array.getBoolean(R.styleable.virtualLine_isHorizontal, true);
        color = array.getColor(R.styleable.virtualLine_lineColor, ContextCompat.getColor(context, R.color.color_e6e6e6));
        dashGap = array.getDimensionPixelSize(R.styleable.virtualLine_dashGap, 0);
        dashWidth = array.getDimensionPixelSize(R.styleable.virtualLine_dashWidth, 0);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(getHeight());
        if (dashGap == 0) {
            setBackgroundColor(color);
        } else {
            drawHorizonLine(canvas);
        }
    }

    private void drawHorizonLine(Canvas canvas) {
        mPath.moveTo(0, 0);
        mPath.lineTo(getMeasuredWidth(), 0);
        PathEffect effects = new DashPathEffect(new float[]{30, 10}, 1.0f);
        mPaint.setPathEffect(effects);
        canvas.drawPath(mPath, mPaint);
        mPaint.setPathEffect(effects);
        canvas.drawPath(mPath, mPaint);
    }

    public void setColor(int color) {
        this.color = color;
        postInvalidate();
    }

    public void setDashGap(float dashGap) {
        this.dashGap = dashGap;
        postInvalidate();
    }

    public void setDashWidth(float dashWidth) {
        this.dashWidth = dashWidth;
        postInvalidate();
    }
}
