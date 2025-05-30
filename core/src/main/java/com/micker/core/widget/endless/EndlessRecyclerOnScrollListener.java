package com.micker.core.widget.endless;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.micker.core.holder.DefaultFooterViewHolder;


public abstract class EndlessRecyclerOnScrollListener extends
        RecyclerView.OnScrollListener {

    public static String TAG = "EndlessRecyclerOnScrollListener";

    private int lastLoadMoreTotalCount = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private int lastCompletelyVisibleItemPosition;
    private long loadTimeTemp;
    private int currentPage = 1;


    public EndlessRecyclerOnScrollListener() {

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = recyclerView.getChildCount();
        Log.i("load", "visibleItemCOunt = " + visibleItemCount);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        if (layoutManager instanceof LinearLayoutManager) {
            lastCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                    .findLastCompletelyVisibleItemPosition();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager
                    = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            lastCompletelyVisibleItemPosition = findMax(lastPositions);
        }

        View view = layoutManager.findViewByPosition(lastCompletelyVisibleItemPosition);
        if (view != null) {
            Object tag = view.getTag();
            if (tag instanceof Integer && ((Integer) tag == DefaultFooterViewHolder.FOOTER_TAG))
                recyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                Log.i("load", "totalItemCOunt = " + totalItemCount + "; previousTotal = " + previousTotal);
                loading = false;
                previousTotal = totalItemCount;
            }
        }


        if (!loading && (visibleItemCount > 0)
                && (lastCompletelyVisibleItemPosition >= totalItemCount - visibleItemCount)) {
            if (recyclerView.getAdapter() instanceof EndlessRecyclerAdapter) {
                EndlessRecyclerAdapter adapter = (EndlessRecyclerAdapter) recyclerView.getAdapter();
                if (adapter.getFooterCount() > 0
                        && adapter.getHeadCount() + adapter.getFooterCount() < totalItemCount
                        && adapter.isRealLoadMore()
                        && totalItemCount > lastLoadMoreTotalCount) {
                    if (Math.abs(System.currentTimeMillis() - loadTimeTemp) > 500) {
                        Log.i(TAG, String.valueOf(currentPage));
                        loadTimeTemp = System.currentTimeMillis();
                        currentPage++;
                        lastLoadMoreTotalCount = totalItemCount;
                        onLoadMore(currentPage);
                        Log.i("load", "onLoadMore, visibleItemCount = " + visibleItemCount
                                + "; totalItemCOunt = " + totalItemCount
                                + "; lastComletelyVisibleItemPosisition = " + lastCompletelyVisibleItemPosition
                                + "; loading = " + loading);
                        loading = true;
                    }
                }
            }
        }
    }


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    public abstract void onLoadMore(int currentPage);

    public void clearPage() {
        currentPage = 1;
        loading = false;
        previousTotal = 0;
        lastLoadMoreTotalCount = 0;
    }

    public void loadingError() {
        loading = false;
        currentPage--;
        previousTotal = 0;
        lastLoadMoreTotalCount = 0;
    }

    public void onRefreshLoading() {
        loading = true;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}