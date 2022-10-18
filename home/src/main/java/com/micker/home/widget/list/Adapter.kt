package com.micker.home.widget.list

import android.view.View
import android.view.ViewGroup

interface Adapter {
    fun getItemCount() : Int
    fun onCreateViewHodler(position: Int, convertView: View?, parent: ViewGroup): View?
    fun onBinderViewHodler(position: Int, convertView: View, parent: ViewGroup)
    fun getItemViewType(position: Int): Int
}