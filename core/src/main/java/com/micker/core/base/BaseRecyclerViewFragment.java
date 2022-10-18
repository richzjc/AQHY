package com.micker.core.base;

import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.micker.core.R;
import com.micker.core.adapter.BaseRecycleAdapter;
import com.micker.core.callback.BaseRecyclerViewCallBack;
import com.micker.core.widget.CustomRecycleView;
import com.micker.core.widget.PullToRefreshCustomRecyclerView;
import com.micker.core.widget.endless.ILoadMorePageListener;
import com.micker.core.widget.pulltorefresh.IRefreshListener;

import java.util.List;

/**
 * Created by zhangyang on 16/7/8.
 */
public abstract class BaseRecyclerViewFragment<D, V, T extends BasePresenter<V>> extends BaseFragment<V, T>
        implements IRefreshListener, ILoadMorePageListener, BaseRecyclerViewCallBack<D> {

    protected PullToRefreshCustomRecyclerView ptrRecyclerView;
    protected CustomRecycleView recycleView;
    protected BaseRecycleAdapter adapter;
    private RecyclerView.OnScrollListener onScrollListener;

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        if (recycleView != null) {
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    @Override
    public int doGetContentViewId() {
        return R.layout.base_fragment_recycle;
    }

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        ptrRecyclerView = mViewQuery.findViewById(R.id.recycleView);
        recycleView = ptrRecyclerView.getCustomRecycleView();
        ptrRecyclerView.setRefreshListener(this);
        recycleView.setLoadMorePageListener(this);
        if (adapter == null) {
            adapter = doInitAdapter();
        }
        if (onScrollListener != null && recycleView != null) {
            recycleView.clearOnScrollListeners();
            recycleView.addOnScrollListener(onScrollListener);
        }
    }

    @Nullable
    public abstract BaseRecycleAdapter doInitAdapter();

    @Override
    public void doInitData() {
        super.doInitData();
        initAdapter();
    }

    private void initAdapter() {
        if (recycleView.getAdapter() == null) {
            if (adapter == null) {
                adapter = doInitAdapter();
            }
            recycleView.setAdapter(adapter);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (adapter != null) {
            adapter.clearData();
            adapter = null;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

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
    public void onResponseError(int code) {
        ptrRecyclerView.onRefreshComplete();
        isListFinish(true);
        recycleView.onLoadingError();
        recycleView.changeToMerginState();
        if (adapter != null && adapter.getListItemCount() > 0)
            return;
//        if (code == ErrorCode.EMPTYURL) {
//            viewManager.showNetworkErrorView();
//        } else {
//            viewManager.showLoadErrorView();
//        }
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

    public void autoRefresh() {
        try {
            recycleView.scrollToPosition(0);
            ptrRecyclerView.autoRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
