package com.micker.home.adapter

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.home.holder.HomeCategoryListViewHolder

class HomeCategoryListAdapter : BaseRecycleAdapter<HomeSubItemEntity, HomeCategoryListViewHolder>(){
    override fun binderItemHolder(holder: HomeCategoryListViewHolder?, position: Int) {
        holder?.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): HomeCategoryListViewHolder {
        return HomeCategoryListViewHolder(parent?.context)
    }
}