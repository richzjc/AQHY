package com.micker.core.base

import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * Created by zhangjianchuan on 2016/6/27.
 */
abstract class BasePresenter<T> {
    protected var mViewRef: Reference<T>? = null

    val viewRef: T?
        get() = if (null != mViewRef) {
            mViewRef!!.get()
        } else null

    val isViewRefAttached: Boolean
        get() = mViewRef != null && mViewRef!!.get() != null

    fun attachViewRef(view: T?) {
        if (mViewRef == null) {
            mViewRef = WeakReference(view)
        }
    }

    fun detachViewRef() {
        if (mViewRef != null) {
            mViewRef!!.clear()
            mViewRef = null
        }
    }
}
