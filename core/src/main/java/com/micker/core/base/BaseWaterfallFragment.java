package com.micker.core.base;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.micker.core.R;
import com.micker.core.widget.TitleBar;
import com.micker.helper.system.ScreenUtils;

/**
 * Created by zhangyang on 16/7/8.
 */
public abstract class BaseWaterfallFragment<D, V, T extends BasePresenter<V>> extends BaseRecyclerViewFragment<D, V, T> {

    protected TitleBar titleBar;

    @Override
    public int doGetContentViewId() {
        return R.layout.aqhy_fragment_waterfall;
    }

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        titleBar = view.findViewById(R.id.titlebar);
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        addItemDecoration();
    }

    public void addItemDecoration() {
        recycleView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanIndex = layoutParams.getSpanIndex();
                outRect.top = 0;
                outRect.bottom = ScreenUtils.dip2px(10f);
                if (spanIndex == 0) {
                    // left
                    outRect.left = ScreenUtils.dip2px(10f);
                    outRect.right = ScreenUtils.dip2px(10f);
                } else {
                    outRect.right = ScreenUtils.dip2px(10f);
                    outRect.left = 0;
                }
            }
        });
    }
}
