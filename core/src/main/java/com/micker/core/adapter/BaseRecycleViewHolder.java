package com.micker.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.micker.core.internal.ViewQuery;
import com.micker.core.widget.RecyclerViewLayout;

/**
 * Created by micker on 16/6/17.
 */
public abstract class BaseRecycleViewHolder<T> extends RecyclerView.ViewHolder implements LifecycleOwner {

    protected T content;
    protected ViewQuery mViewQuery;
    protected Context mContext;
    protected View actualItemView;
    protected View skeletonItemView;
    protected RecyclerViewLayout itemContainer;
    private LifecycleRegistry mLifecycleRegistry;

    protected BaseRecycleViewHolder(View itemView) {
        super(itemView);
    }

    public BaseRecycleViewHolder(Context context) {
        super(new RecyclerViewLayout(context));
        mContext = context;
        itemContainer = (RecyclerViewLayout) itemView;
        if (getLayoutId() != 0 && itemContainer.getChildCount() == 0) {
            actualItemView = LayoutInflater.from(mContext).inflate(getLayoutId(), itemContainer, false);
            itemContainer.addView(actualItemView);
        }

        if (actualItemView != null) {
            ViewGroup.LayoutParams params = actualItemView.getLayoutParams();
            ViewGroup.MarginLayoutParams itemParams = new ViewGroup.MarginLayoutParams(params.width,
                    params.height);
            itemContainer.setLayoutParams(itemParams);
            mViewQuery = new ViewQuery();
            mViewQuery.setView(itemContainer);
            doBindView(actualItemView);
        }
    }

    protected void setFullSpan() {
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public abstract int getLayoutId();

    protected void doBindView(View itemView) {

    }


    public abstract void doBindData(T content);

    public void doBindData(T content, Object params) {

    }

    public T getContent() {
        return content;
    }

    public void onDetachView() {

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("V2     holderName = ")
                .append(getClass().getSimpleName())
                .append("; holderView = ")
                .append(itemView.getClass().getSimpleName()).append("; holderItemViewType = ").append(getItemViewType());
        return builder.toString();
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        if (mLifecycleRegistry == null)
            mLifecycleRegistry = new LifecycleRegistry(this);
        return mLifecycleRegistry;
    }
}
