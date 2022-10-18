package com.micker.core.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.micker.helper.system.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micker on 16/6/17.
 */
public abstract class BaseRecycleAdapter<D, T extends RecyclerView.ViewHolder> extends BaseAnimationAdapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_HEADER_START = 1000;
    protected static final int TYPE_FOOTER = 2000;
    protected static final int TYPE_EMPTY_START = 3000;

    private List<View> mHeaderViews = new ArrayList<>();
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;
    private AdapterDataDelegate<D> delegate;
    protected String editTextColor;

    public BaseRecycleAdapter() {
        super();
        delegate = new AdapterDataDelegate<>(this);
        setHasStableIds(true);
    }

    @Override
    protected Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0).setDuration(200)
        };
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder)holder).doBindData();
            return;
        } else if (holder instanceof FooterHolder) {

            return;
        } else if (holder instanceof EmptyHolder) {
            ((EmptyHolder) holder).bindData();
            return;
        }
        if (holder instanceof BaseRecycleViewHolder) {
            try {
                binderItemHolder((T) holder, getListItemPosition(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean dataIsNullOrEmpty() {
        return delegate.isEmpty() || delegate.isListEmpty();
    }

    public void clearData() {
        mHeaderViews.clear();
        delegate.clear();
        mFooterView = null;
        notifyDataSetChanged();
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        notifyDataSetChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER_START) {
            View v = mHeaderView;
            return new HeaderHolder(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = mFooterView;
            return new FooterHolder(v);
        } else if (viewType == TYPE_EMPTY_START) {
            View v = mEmptyView;
            return new EmptyHolder(v);
        }
        T holder = createListItemView(parent, viewType);
        return holder;
    }

    @Override
    public int getItemCount() {
        int size = getListItemCount();
        if (size == 0 && delegate.isEmpty()) {
            return 1;
        }
        if (size == 0 && delegate.isListEmpty() && null != mEmptyView) {
            return 1 + getHeadViewSize() + getFooterViewSize();
        } else {
            return getHeadViewSize() + size + getFooterViewSize();
        }
    }

    @Override
    public final int getItemViewType(int position) {
        int size = getListItemCount();
        if (position < getHeadViewSize()) {
            mHeaderView = mHeaderViews.get(position);
            return TYPE_HEADER_START;
        } else if (size == 0 && delegate.isListEmpty() && null != mEmptyView) {
            return TYPE_EMPTY_START;
        } else if (position >= getHeadViewSize() + getListItemCount()) {
            return TYPE_FOOTER;
        }
        try {
            return getListType(getListItemPosition(position));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getListType(int listItemPosition) {
        return 0;
    }


    public void setData(List<D> data) {
        delegate.setData(data);
    }

    public void notifyItemChanged() {
        delegate.notifyItemChanged();
    }

    public void onItemMove(int lastPos, int nowPos) {
        delegate.onItemMove(lastPos, nowPos);
    }


    public D getItemAtPosition(int pos) {
        if (pos < getHeadViewSize()) {
            return null;
        }
        if (pos >= (getHeadViewSize() + getListItemCount())) {
            return null;
        }
        if (delegate.isEmpty()) {
            return null;
        }

        return delegate.getData(getListItemPosition(pos));
    }

    @Override
    public long getItemId(int position) {
        return Integer.MAX_VALUE + position;
    }

    public int getHeadViewSize() {
        return mHeaderViews.size();
    }

    public int getFooterViewSize() {
        return mFooterView == null ? 0 : 1;
    }

    public int getListItemCount() {
        return delegate.getListSize();
    }

    public int getListItemPosition(int pos) {
        return pos - getHeadViewSize();
    }

    public abstract void binderItemHolder(T holder, int position);

    public abstract T createListItemView(ViewGroup parent, int viewType);


    //add a header to the adapter
    public void addHeader(View header) {
        Log.i("addHeader", String.valueOf(mHeaderViews.contains(header)));
        if (!mHeaderViews.contains(header)) {
            mHeaderViews.add(header);
            notifyDataSetChanged();
        }
    }

    //remove a header from the adapter
    public void removeHeader(View header) {
        if (mHeaderViews.contains(header)) {
            notifyItemRemoved(mHeaderViews.indexOf(header));
            mHeaderViews.remove(header);
        }
    }

    public void removeAllHeader() {
        mHeaderViews.clear();
        notifyDataSetChanged();
    }

    //add a footer to the adapter
    public void addFooter(View footer) {
        mFooterView = footer;
        notifyItemInserted(getItemCount() + 1);
        // notifyDataSetChanged();
    }

    //remove a footer from the adapter
    public void removeFooter() {
        mFooterView = null;
        notifyDataSetChanged();
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {

        HeaderHolder(View itemView) {
            super(itemView);
//            setFullSpan();
        }

        public void doBindData(){
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp != null) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, lp.height);
                layoutParams.setFullSpan(true);
                itemView.setLayoutParams(layoutParams);
            }else{
                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                itemView.setLayoutParams(layoutParams);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("V2     holderName = ")
                    .append(getClass().getSimpleName())
                    .append("; holderView = ")
                    .append(itemView.getClass().getSimpleName())
                    .append("; holderItemViewType = " + getItemViewType());
            return builder.toString();
        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {
        FooterHolder(View itemView) {
            super(itemView);
            setFullSpan();
        }

        void setFullSpan() {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("V2     holderName = ")
                    .append(getClass().getSimpleName())
                    .append("; holderView = ")
                    .append(itemView.getClass().getSimpleName())
                    .append("; holderItemViewType = " + getItemViewType());
            return builder.toString();
        }
    }

    protected static class EmptyHolder extends RecyclerView.ViewHolder {
        AnimatorSet animatorSet;

        EmptyHolder(View itemView) {
            super(itemView);
            setFullSpan();
        }

        void setFullSpan() {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("V2     holderName = ")
                    .append(getClass().getSimpleName())
                    .append("; holderView = ")
                    .append(itemView.getClass().getSimpleName())
                    .append("; holderItemViewType = " + getItemViewType());
            return builder.toString();
        }

        public void bindData() {
            showAnimation();
        }

        public void detachView() {
            disAnimation();
        }

        public void showAnimation() {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(itemView, "alpha", 0, 1);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(itemView, "scaleX", 0, 1);
            ObjectAnimator animator3 = ObjectAnimator.ofFloat(itemView, "scaleY", 0, 1);
            if (animatorSet != null)
                animatorSet.cancel();
            animatorSet = new AnimatorSet();
            animatorSet.setDuration(500);
            animatorSet.playTogether(animator1, animator2, animator3);
            animatorSet.start();
        }

        public void disAnimation() {
            if (animatorSet != null) {
                animatorSet.cancel();
            }
        }
    }

    public void onAdapterItemClick(View view, RecyclerView.ViewHolder holder) {
        try {
            if (adapterItemClickListener != null) {
                adapterItemClickListener.onViewClick(view, getItemAtPosition(holder.getAdapterPosition()),
                        holder.getAdapterPosition() - getHeadViewSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof BaseRecycleViewHolder) {
            ((BaseRecycleViewHolder) holder).onDetachView();
        } else if (holder instanceof EmptyHolder) {
            ((EmptyHolder) holder).detachView();
        }
    }

    protected AdapterItemClickListener<D> adapterItemClickListener;

    public void setAdapterItemClickListener(AdapterItemClickListener<D> adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public interface AdapterItemClickListener<D> {
        void onViewClick(View view, D entity, int position);
    }

    public List<D> get() {
        return delegate.getData();
    }

    public void setEditTextColor(String editTextColor) {
        this.editTextColor = editTextColor;
    }

    public boolean isEmpty() {
        return delegate.isEmpty() || delegate.isListEmpty();
    }


    public D get(int pos) {
        return delegate.getData(pos);
    }


    public void removeEntity(D item) {
        delegate.removeItem(item);
    }

    public void diffDetectMoves(boolean diffDetectMoves) {
        delegate.diffDetectMoves(diffDetectMoves);
    }
}