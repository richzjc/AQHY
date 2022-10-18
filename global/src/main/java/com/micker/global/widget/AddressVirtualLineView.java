package com.micker.global.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import com.micker.global.R;

public class AddressVirtualLineView extends View {

    Bitmap bitmap;
    Paint paint;
    public AddressVirtualLineView(Context context) {
        this(context, null);
    }

    public AddressVirtualLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddressVirtualLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Resources r = this.getContext().getResources();
        bitmap = BitmapFactory.decodeResource(r, R.drawable.address_virtrual_line);
        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (bitmap == null)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        else {
            int height = bitmap.getHeight();
            int width = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            int drawWidth = 0;
            int drawableWidth = bitmap.getWidth();
            int viewWidth = getWidth();
            Log.i("address", "viewWidth : " + viewWidth + "; drawableWidth : " + drawableWidth);
            while (drawWidth < viewWidth) {
                canvas.drawBitmap(bitmap, 0, 0, paint);
                drawWidth += drawableWidth;
                canvas.translate(drawableWidth, 0);
            }
        }
    }
}
