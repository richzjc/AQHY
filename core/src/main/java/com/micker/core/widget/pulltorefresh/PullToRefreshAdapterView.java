package com.micker.core.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * Created by ichongliang on 05/03/2018.
 */

public class PullToRefreshAdapterView extends SmartRefreshLayout implements OnRefreshListener {

    private IRefreshListener refreshListener;
    private RefreshHeader    header;
    private boolean isCanRefresh = true;

    public PullToRefreshAdapterView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        header = new SmartRefreshHeaderView(getContext());
        setRefreshHeader(header, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setOnRefreshListener(this);
        setRefreshFooter(new FalsifyFooter(getContext()));
        setEnableLoadMore(false);
    }

    public void setCanRefresh(boolean canRefresh) {
        isCanRefresh = canRefresh;
    }

    public void setRefreshListener(IRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void onRefreshComplete() {
        refreshComplete();
    }

    @Override
    public boolean isEnableRefresh() {
        return isCanRefresh && super.isEnableRefresh();
    }

    public boolean checkCanDoRefresh(ViewGroup frame, View content, View header) {
        return isEnableRefresh();
    }

    public void onRefreshBegin(ViewGroup frame) {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    public void refreshComplete() {
        finishRefresh();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }
}
