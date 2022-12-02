package com.micker.first.holder

import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.first.model.ChineseWordModel
import android.content.Context
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_recycler_item_chinese_word.view.*

class ChildWordViewHolder(context : Context?) : BaseRecycleViewHolder<ChineseWordModel>(context) {

    override fun getLayoutId() = R.layout.aqhy_recycler_item_chinese_word

    override fun doBindData(content: ChineseWordModel?) {
        itemView?.word?.text = content?.word
    }
}