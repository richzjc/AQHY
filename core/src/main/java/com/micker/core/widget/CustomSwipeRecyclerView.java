package com.micker.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.micker.core.adapter.BaseRecycleAdapter;
import com.micker.core.adapter.RVLinearLayoutManager;
import com.micker.core.callback.IViewHolder;
import com.micker.core.holder.DefaultEmptyViewHolder;
import com.micker.core.holder.DefaultFooterViewHolder;
import com.micker.core.widget.endless.EndlessRecyclerAdapter;
import com.micker.core.widget.endless.EndlessRecyclerOnScrollListener;
import com.micker.core.widget.endless.ILoadMorePageListener;
import com.micker.core.widget.endless.IViewLoadAdapter;
import com.tubb.smrv.SwipeMenuRecyclerView;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Leif Zhang on 2017/2/21.
 * Email leifzhanggithub@gmail.com
 */

public class CustomSwipeRecyclerView extends FrameLayout {

    private ILoadMorePageListener loadMorePageListener;
    private EndlessRecyclerOnScrollListener listener;
    private DefaultEmptyViewHolder viewHolder;
    private IViewHolder footerViewHolder;
    private boolean isEndless = true;
    private EndlessRecyclerAdapter mAdapter;
    public SplashView splashView;
    public SwipeMenuRecyclerView recyclerView;

    public CustomSwipeRecyclerView(Context context) {
        super(context);
        initView();
    }

    public CustomSwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomSwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        addContentView();
        setLayoutManager(new RVLinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        viewHolder = new DefaultEmptyViewHolder(this);
        footerViewHolder = new DefaultFooterViewHolder(this);
        setListener();
    }
    public void start(){
        splashView.start();
    }

    public void changeToMerginState(){
        splashView.changeStateToMerginState();
    }

    private void addContentView() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        recyclerView = new SwipeMenuRecyclerView(getContext());
        addView(recyclerView, params);
        splashView = new SplashView(getContext());
        addView(splashView, params);
        splashView.setVisibility(View.GONE);
    }


    private void setListener() {
        ((DefaultFooterViewHolder) footerViewHolder).setOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadMorePageListener != null && isEndless && listener != null) {
                    ((DefaultFooterViewHolder) footerViewHolder).showLoading();
                    loadMorePageListener.onLoadMore(listener.getCurrentPage());
                }
            }
        });

        listener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                if (loadMorePageListener != null && isEndless) {
                    ((DefaultFooterViewHolder) footerViewHolder).showLoading();
                    loadMorePageListener.onLoadMore(currentPage);
                }
            }
        };
        recyclerView.addOnScrollListener(listener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseRecycleAdapter) {
            BaseRecycleAdapter mRecycleAdapter = (BaseRecycleAdapter) adapter;
            mRecycleAdapter.setEmptyView(viewHolder.getView());
            mAdapter = new EndlessRecyclerAdapter(mRecycleAdapter);
            addFooter();
            if (lookup != null) {
                lookup.setAdapter(mAdapter);
            }
            recyclerView.swapAdapter(mAdapter, false);
        } else {
            recyclerView.setAdapter(adapter);
        }
    }

    private void addFooter() {
        if (isEndless) {
            mAdapter.addFooterView(footerViewHolder.getView());
        }
        if (footerViewHolder instanceof IViewLoadAdapter) {
            ((IViewLoadAdapter) footerViewHolder).isRecyclerEnd(isEndless);
        }
    }

    public void setIsEndless(boolean isEndless) {
        this.isEndless = isEndless;
        ((IViewLoadAdapter) footerViewHolder).isRecyclerEnd(isEndless);
    }


    public void hideFooter(boolean isHide) {
        isEndless = !isHide;
        ((IViewLoadAdapter) footerViewHolder).isRecyclerEnd(!isHide);
        if (!isHide && mAdapter.getFooterCount() == 0) {
            mAdapter.addFooterView(footerViewHolder.getView());
        }
    }

    public void setFooterViewHolder(IViewHolder footerViewHolder) {
        this.footerViewHolder = footerViewHolder;
    }

    public void setLoadMorePageListener(ILoadMorePageListener loadMorePageListener) {
        this.loadMorePageListener = loadMorePageListener;
    }

    public void onRefreshComplete() {
        listener.clearPage();
    }

    public void onLoadingError() {
        listener.loadingError();
    }

    private HeaderSpanSizeLookup lookup;

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            if (((GridLayoutManager) layout).getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                lookup = new HeaderSpanSizeLookup(((GridLayoutManager) layout).getSpanCount());
                ((GridLayoutManager) layout).setSpanSizeLookup(lookup);
            }
        }
        recyclerView.setLayoutManager(layout);
    }

    public int getCurItemPosition() {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int firstCompletelyItemPosition = 0;
        if (layoutManager instanceof GridLayoutManager) {
            firstCompletelyItemPosition = ((GridLayoutManager) layoutManager)
                    .findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            firstCompletelyItemPosition = ((LinearLayoutManager) layoutManager)
                    .findFirstCompletelyVisibleItemPosition();
        }
        return firstCompletelyItemPosition;
    }

    public void setEmptyTv(String text) {
        viewHolder.setEmptyText(text);
    }

    public void setEmptyBtn(int visible, String btnText, OnClickListener listener) {
        viewHolder.setTvBtn(visible, btnText, listener);
    }

    public void setEmptyImageRes(int resId) {
        viewHolder.setEmptyImageRes(resId);
    }

    public final void onRefresh() {
        listener.onRefreshLoading();
    }
    public void addItemDecoration(@NotNull DividerItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener);
    }

    public RecyclerView.Adapter getAdapter() {
        return recyclerView.getAdapter();
    }

    public void scrollToPosition(int i) {
        recyclerView.scrollToPosition(i);
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        recyclerView.removeOnScrollListener(onScrollListener);
    }
}
