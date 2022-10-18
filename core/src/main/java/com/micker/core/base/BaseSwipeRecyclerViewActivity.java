package com.micker.core.base;

import android.view.View;
import androidx.annotation.Nullable;
import com.micker.core.R;
import com.micker.core.adapter.BaseRecycleAdapter;
import com.micker.core.callback.BaseRecyclerViewCallBack;
import com.micker.core.widget.CustomSwipeRecyclerView;
import com.micker.core.widget.PullToRefreshSwipeRecyclerView;
import com.micker.core.widget.TitleBar;
import com.micker.core.widget.endless.ILoadMorePageListener;
import com.micker.core.widget.pulltorefresh.IRefreshListener;

import java.util.List;

/**
 * Created by Leif Zhang on 16/7/22.
 * Email leifzhanggithub@gmail.com
 */
public abstract class BaseSwipeRecyclerViewActivity<D, V, T extends BasePresenter<V>>
        extends BaseActivity<V, T> implements ILoadMorePageListener, IRefreshListener,
        BaseRecyclerViewCallBack<D> {

    protected PullToRefreshSwipeRecyclerView ptrRecyclerView;
    protected CustomSwipeRecyclerView recycleView;
    protected TitleBar titleBar;
    protected BaseRecycleAdapter adapter;

    @Override
    public int doGetContentViewId() {
        return R.layout.base_swipe_activity_recycle;
    }

    @Nullable
    public abstract BaseRecycleAdapter doInitAdapter();

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        ptrRecyclerView = getMViewQuery().findViewById(R.id.recycleView);
        titleBar = getMViewQuery().findViewById(R.id.titlebar);
        recycleView = ptrRecyclerView.getCustomRecycleView();
        ptrRecyclerView.setRefreshListener(this);
        recycleView.setLoadMorePageListener(this);
    }

    @Override
    public void doInitData() {
        super.doInitData();
        if (adapter == null)
            adapter = doInitAdapter();
        recycleView.setAdapter(adapter);
    }


    @Override
    public void setData(List<D> results, boolean isCache) {
        if (!isCache) {
            ptrRecyclerView.onRefreshComplete();
        }
        if (adapter == null)
            adapter = doInitAdapter();
        adapter.setData(results);
        recycleView.changeToMerginState();
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
    public void notifyDataRangeChange() {
        adapter.notifyItemChanged();
    }

    @Override
    public void isListFinish(boolean result) {
        recycleView.hideFooter(result);
    }
}
