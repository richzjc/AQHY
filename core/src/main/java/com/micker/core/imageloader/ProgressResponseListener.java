package com.micker.core.imageloader;

/**
 * Created by zhangyang on 16/1/23.
 */
public interface ProgressResponseListener<T> {

    void onComplete(T source);
}
