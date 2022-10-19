package com.micker.aqhy.holder

import com.micker.aqhy.model.MainGridModel
import com.micker.core.adapter.BaseRecycleViewHolder
import android.content.Context
import com.micker.aqhy.R
import com.micker.helper.router.RouterHelper
import kotlinx.android.synthetic.main.aqhy_recycler_item_main_grid.view.*

class MainGridHolder(context : Context?) : BaseRecycleViewHolder<MainGridModel>(context) {
    init {
        itemView?.setOnClickListener {
            RouterHelper.open(super.content.router, mContext)
        }
    }
    override fun getLayoutId() = R.layout.aqhy_recycler_item_main_grid

    override fun doBindData(content: MainGridModel?) {
        super.content = content
        itemView?.share_tv?.text = content?.name
    }
}