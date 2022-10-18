package com.micker.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.micker.core.widget.pulltorefresh.PullToRefreshAdapterView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * Created by zhangyang on 16/6/24.
 */
public class PullToRefreshCustomRecyclerView extends PullToRefreshAdapterView {
    public PullToRefreshCustomRecyclerView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshCustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshCustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Nullable
    private CustomRecycleView customRecycleView;

    private void init() {
        customRecycleView = getCustomRecycleView();
        addView(customRecycleView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
//        disableWhenHorizontalMove(true);
    }

    @Override
    public void onRefreshComplete() {
        super.onRefreshComplete();
        getCustomRecycleView().onRefreshComplete();
    }


    public CustomRecycleView getCustomRecycleView() {
        if (customRecycleView == null)
            customRecycleView = new CustomRecycleView(getContext());
        return customRecycleView;
    }

    @Override
    public boolean checkCanDoRefresh(ViewGroup frame, View content, View header) {
        return super.checkCanDoRefresh(frame, content, header);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
    }

    @Override
    public final void onRefreshBegin(ViewGroup frame) {
        super.onRefreshBegin(frame);
        customRecycleView.onRefresh();
    }
}
