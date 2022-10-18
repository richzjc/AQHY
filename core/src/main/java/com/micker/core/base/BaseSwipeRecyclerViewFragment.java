package com.micker.core.base;

import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.micker.core.R;
import com.micker.core.adapter.BaseRecycleAdapter;
import com.micker.core.callback.BaseRecyclerViewCallBack;
import com.micker.core.widget.CustomSwipeRecyclerView;
import com.micker.core.widget.PullToRefreshSwipeRecyclerView;
import com.micker.core.widget.endless.ILoadMorePageListener;
import com.micker.core.widget.pulltorefresh.IRefreshListener;

import java.util.List;

/**
 * Created by Leif Zhang on 2017/2/21.
 * Email leifzhanggithub@gmail.com
 */

public abstract class BaseSwipeRecyclerViewFragment<D, V, T extends BasePresenter<V>> extends BaseFragment<V, T>
        implements ILoadMorePageListener, IRefreshListener, BaseRecyclerViewCallBack<D> {

    protected PullToRefreshSwipeRecyclerView ptrRecyclerView;
    protected CustomSwipeRecyclerView recycleView;
    protected BaseRecycleAdapter adapter;

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        ptrRecyclerView = mViewQuery.findViewById(R.id.recycleView);
        recycleView = ptrRecyclerView.getCustomRecycleView();
        recycleView.setLoadMorePageListener(this);
        ptrRecyclerView.setRefreshListener(this);
        if (adapter == null) {
            adapter = doInitAdapter();
        }
        if (onScrollListener != null && isVisibleToUser) {
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    private RecyclerView.OnScrollListener onScrollListener;

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    public int doGetContentViewId() {
        return R.layout.base_fragment_recycler_swipe;
    }

    @Nullable
    public abstract BaseRecycleAdapter doInitAdapter();

    @Override
    public void setData(List<D> results, boolean isCache) {
        if (!isCache) {
            ptrRecyclerView.onRefreshComplete();
        }
        if (adapter == null) {
            adapter = doInitAdapter();
            recycleView.setAdapter(adapter);
        }
        adapter.setData(results);
        recycleView.changeToMerginState();
    }


    @Override
    public void doInitData() {
        super.doInitData();
        initAdapter();
        if (onScrollListener != null && recycleView != null) {
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    private void initAdapter() {
        if (recycleView.getAdapter() == null && getUserVisibleHint()) {
            if (adapter == null) {
                adapter = doInitAdapter();
            }
            recycleView.setAdapter(adapter);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onScrollListener != null && recycleView != null) {
            recycleView.removeOnScrollListener(onScrollListener);
        }
    }

    public void autoRefresh() {
        try {
            recycleView.scrollToPosition(0);
            ptrRecyclerView.autoRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyDataRangeChange() {
        if (isAdded() && adapter != null) {
            adapter.notifyItemChanged();
        }
    }

    @Override
    public void isListFinish(boolean result) {
        if (isAdded()) {
            recycleView.hideFooter(result);
        }
    }

    @Override
    public void onResponseError(int code) {
        ptrRecyclerView.onRefreshComplete();
        recycleView.onLoadingError();
        isListFinish(true);
        recycleView.changeToMerginState();
        if (adapter != null && adapter.getListItemCount() > 0)
            return;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (adapter != null) {
            adapter.clearData();
            adapter = null;
        }
    }

}