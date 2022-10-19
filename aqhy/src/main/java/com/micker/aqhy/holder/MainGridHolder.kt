package com.micker.aqhy.holder

import com.micker.aqhy.model.MainGridModel
import com.micker.core.adapter.BaseRecycleViewHolder
import android.content.Context
import com.micker.aqhy.R
import kotlinx.android.synthetic.main.aqhy_recycler_item_main_grid.view.*

class MainGridHolder(context : Context?) : BaseRecycleViewHolder<MainGridModel>(context) {
    override fun getLayoutId() = R.layout.aqhy_recycler_item_main_grid

    override fun doBindData(content: MainGridModel?) {
        itemView?.share_tv?.text = content?.name
    }
}