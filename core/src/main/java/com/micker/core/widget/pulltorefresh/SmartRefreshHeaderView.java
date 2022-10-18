package com.micker.core.widget.pulltorefresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.micker.core.R;
import com.micker.core.imageloader.ImageLoadManager;
import com.micker.core.imageloader.WscnImageView;
import com.micker.helper.image.ImageUtlFormatHelper;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * Created by ichongliang on 05/03/2018.
 */

public class SmartRefreshHeaderView extends RelativeLayout implements RefreshHeader {

    protected SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;

    public SmartRefreshHeaderView(Context context) {
        super(context);
        init(context, null);
    }

    public SmartRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    //	protected final ImageView mHeaderImage;
    public WscnImageView mIvRefresh;
    private TextView mHeaderText;
    private WscnImageView adImageView;

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.base_refresh_header_vertical, this);
        mHeaderText = (TextView) view.findViewById(R.id.pull_to_refresh_text);
        mIvRefresh = (WscnImageView) view.findViewById(R.id.iv_refresh);
        adImageView = (WscnImageView) view.findViewById(R.id.adIv);
        adImageView.getHierarchy().setPlaceholderImage(new ColorDrawable(Color.TRANSPARENT));
        ImageLoadManager.loadImage(R.drawable.base_ptr_refresh, mIvRefresh, 0);
        setImageLayoutParams(PTRHeaderConfig.scale);
        loadImage(PTRHeaderConfig.REFRESH_ICON);
        setPullStr(PTRHeaderConfig.REFRESH_PULL_TEXT);
        setLoadingStr(PTRHeaderConfig.REFRESH_REFRESHING_TEXT);
    }

    public void setImageLayoutParams(float scale) {
        if (scale < 0.3) {
            return;
        }
        //  scale = scale > 0.3f ? 0.3f : scale;
        ViewGroup.LayoutParams params = adImageView.getLayoutParams();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        params.width = dm.widthPixels;
        params.height = (int) (dm.widthPixels / scale);
        adImageView.setLayoutParams(params);
    }


    public void loadImage(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        String imgUrl = ImageUtlFormatHelper.formatImageWithThumbnail(imageUrl, 640, 0);
        ImageLoadManager.loadImage(imgUrl, adImageView, 0);
    }

    private String pullStr;
    private String loadingStr;

    public void setPullStr(String pullStr) {
        this.pullStr = pullStr;
    }

    public void setLoadingStr(String loadingStr) {
        this.loadingStr = loadingStr;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }


    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
