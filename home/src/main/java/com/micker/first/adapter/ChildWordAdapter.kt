package com.micker.first.adapter

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.first.holder.ChildWordViewHolder
import com.micker.first.model.ChineseWordModel
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter

class ChildWordAdapter : BaseRecycleAdapter<ChineseWordModel, ChildWordViewHolder>(){

    override fun binderItemHolder(holder: ChildWordViewHolder?, position: Int) {
        holder?.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): ChildWordViewHolder {
        return ChildWordViewHolder(parent?.context)
    }
}