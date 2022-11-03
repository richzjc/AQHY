package com.micker.child

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter

class NewsMainChildAdapter : BaseRecycleAdapter<NewsMainChildEntity, NewsMainChildHolder>() {
    override fun binderItemHolder(holder: NewsMainChildHolder?, position: Int) {
        holder?.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): NewsMainChildHolder {
       return NewsMainChildHolder(parent?.context)
    }
}