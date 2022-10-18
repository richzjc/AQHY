package com.micker.home.adapter

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.data.model.home.HomeEntity
import com.micker.home.holder.HomeViewHolder

class HomeAdapter : BaseRecycleAdapter<HomeEntity, HomeViewHolder>(){
    override fun binderItemHolder(holder: HomeViewHolder?, position: Int) {
        holder?.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int) = HomeViewHolder(parent?.context)
}