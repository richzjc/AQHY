package com.micker.global.api

import android.os.Parcelable

class BaseListModel<T : Parcelable> : ArrayList<T>(){

    private var isTouchEnd = false
    private var limit = 20
    var page = 1
    private val isCache: Boolean = false

    fun getCounter(): Int {
        return page
    }

    fun updateCounter() {
        page++
    }

    fun getLimit(): Int {
        return limit
    }

    fun setLimit(limit: Int) {
        this.limit = limit
    }

    fun isTouchEnd(): Boolean {
        return isTouchEnd
    }

    fun setTouchEnd(touchEnd: Boolean) {
        isTouchEnd = touchEnd
    }

    fun clearPage() {
        page = 1
    }

    fun isRefresh(): Boolean {
        return page == 1
    }

    fun addList(list: List<T>) {
        addAll(list)
    }

    fun checkIsEnd(model: BaseListModel<T>) {
        if (model == null || model!!.isEmpty()) {
            setTouchEnd(true)
        } else {
            setTouchEnd(false)
        }
    }
}