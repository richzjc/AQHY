package com.micker.core.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by micker on 16/6/16.
 */
public class IconView extends AppCompatTextView {
    private Typeface customTypeFace;

    public IconView(Context context) {
        super(context);
        initTypeFace();
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }

    private void initTypeFace() {
        setCustomTypeFace(Typeface.createFromAsset(getContext().getAssets(), "wscniconfont/iconfont.ttf"));
    }

    public void setCustomTypeFace(Typeface customTypeFace) {
        this.customTypeFace = customTypeFace;
        if (null != customTypeFace)
            setTypeface(this.customTypeFace);
    }
}
