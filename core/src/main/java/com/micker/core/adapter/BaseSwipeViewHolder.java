package com.micker.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.micker.core.R;
import com.micker.core.internal.ViewQuery;
import com.micker.core.widget.RecyclerViewLayout;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

/**
 * Created by Leif Zhang on 2017/2/21.
 * Email leifzhanggithub@gmail.com
 */

public abstract class BaseSwipeViewHolder<T> extends BaseRecycleViewHolder<T> {
    public View smMenuViewRight;
    public RecyclerViewLayout smContentView;
    public SwipeHorizontalMenuLayout horizontalMenuLayout;

    public BaseSwipeViewHolder(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.base_recycler_item_swipe, null));
        mContext = context;
        if (mContext == null)
            mContext = itemView.getContext();
        horizontalMenuLayout = (SwipeHorizontalMenuLayout) itemView;
        smContentView = itemView.findViewById(R.id.smContentView);
        itemContainer = smContentView;
        smMenuViewRight = itemView.findViewById(R.id.smMenuViewRight);
        if (getLayoutId() != 0 && smContentView.getChildCount() == 0) {
            actualItemView = LayoutInflater.from(mContext).inflate(getLayoutId(), itemContainer, false);
            smContentView.addView(actualItemView);
        }
        ViewGroup.LayoutParams params = actualItemView.getLayoutParams();
        ViewGroup.LayoutParams itemParams = new ViewGroup.LayoutParams(params.width,
                params.height);
        itemView.setLayoutParams(itemParams);
        mViewQuery = new ViewQuery();
        mViewQuery.setView(itemView);
        doBindView(itemView);
    }


    @Override
    protected void doBindView(View itemView) {

    }
}
