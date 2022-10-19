package com.micker.aqhy.adapter

import android.view.ViewGroup
import com.micker.aqhy.holder.MainGridHolder
import com.micker.aqhy.model.MainGridModel
import com.micker.core.adapter.BaseRecycleAdapter

class MainGridAdapter : BaseRecycleAdapter<MainGridModel, MainGridHolder>(){
    override fun binderItemHolder(holder: MainGridHolder?, position: Int) {
        holder?.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int) = MainGridHolder(parent?.context)
}