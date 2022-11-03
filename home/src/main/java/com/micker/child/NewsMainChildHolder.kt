package com.micker.child

import com.micker.core.adapter.BaseRecycleViewHolder
import android.content.Context
import com.micker.home.R

class NewsMainChildHolder(context: Context?) : BaseRecycleViewHolder<NewsMainChildEntity>(context) {
    init {
        itemView?.setOnClickListener {

        }
    }

    override fun getLayoutId() = R.layout.aqhy_recycler_item_news_main_child

    override fun doBindData(content: NewsMainChildEntity?) {
        super.content = content
    }
}