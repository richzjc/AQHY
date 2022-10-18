package com.micker.core.callback;

import java.util.List;

/**
 * Created by zhangyang on 16/7/8.
 */
public interface BaseRecyclerViewCallBack<T> {
    void setData(List<T> results, boolean isCache);

    void onResponseError(int code);

    void notifyDataRangeChange();

    void isListFinish(boolean result);
}
