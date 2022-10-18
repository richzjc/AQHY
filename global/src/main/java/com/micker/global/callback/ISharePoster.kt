package com.micker.global.callback

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup

interface ISharePoster {
    fun initView(parent: ViewGroup)
    fun bindData(parcel: Parcelable?)
    fun getView(): View?
}