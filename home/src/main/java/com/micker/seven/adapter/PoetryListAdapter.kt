package com.micker.seven.adapter

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.seven.db.SevenModelEnitity
import com.micker.seven.holder.PoetryListViewHolder

class PoetryListAdapter : BaseRecycleAdapter<SevenModelEnitity, PoetryListViewHolder>() {

    override fun binderItemHolder(holder: PoetryListViewHolder?, position: Int) {
        holder?.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): PoetryListViewHolder {
        return PoetryListViewHolder(parent?.context)
    }
}