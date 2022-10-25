package com.wallstreetcn.global.media.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class VideoImageView extends AppCompatImageView {
    public VideoImageView(Context context) {
        super(context);
    }

    public VideoImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
