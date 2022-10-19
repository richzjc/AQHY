package com.micker.core.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import com.micker.core.R;

public class ShareTextView extends AppCompatTextView {


    public ShareTextView(@NonNull Context context) {
        this(context,null);
    }

    public ShareTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShareTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        Typeface typeface = ResourcesCompat.getFont(context, R.font.aorkyork);
        if (typeface != null) {
            setTypeface(typeface);
        }
    }
}
