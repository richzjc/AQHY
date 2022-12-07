package com.micker.seven.holder

import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.seven.db.SevenModelEnitity
import android.content.Context
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_recycler_item_poetry_list.view.*

class PoetryListViewHolder(context: Context?) : BaseRecycleViewHolder<SevenModelEnitity>(context) {
    init {
        itemView?.setOnClickListener {
            //TODO 跳转到详情页面
        }
    }

    override fun getLayoutId() = R.layout.aqhy_recycler_item_poetry_list

    override fun doBindData(content: SevenModelEnitity?) {
        super.content = content
        itemView?.title?.text = content?.title
        itemView?.author?.text = content?.author
        itemView?.content?.text = content?.content
    }
}