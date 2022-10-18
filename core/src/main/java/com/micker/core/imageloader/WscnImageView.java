package com.micker.core.imageloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.micker.core.R;

/**
 * Created by wscn on 16/12/28.
 */

public class WscnImageView extends SimpleDraweeView {

    public WscnImageView(Context context) {
        super(context);
        init();
    }

    public WscnImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WscnImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WscnImageView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        init();
    }

    public void clearImage() {
        setImageURI("");
    }

    private void init() {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        if (!hierarchy.hasPlaceholderImage()) {
            hierarchy.setPlaceholderImage(R.drawable.default_img, ScalingUtils.ScaleType.CENTER_INSIDE);
        }
        hierarchy.setRetryImage(R.color.retry_color);
        hierarchy.setFadeDuration(400);
    }
}
