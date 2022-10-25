package com.micker.five.holder

import com.micker.core.adapter.BaseRecycleViewHolder
import android.content.Context
import com.micker.five.model.FiveStageModel
import com.micker.home.R

class FiveStageHolder(context : Context?) : BaseRecycleViewHolder<FiveStageModel>(context){
    override fun getLayoutId() = R.layout.aqhy_recycler_item_five_stage

    override fun doBindData(content: FiveStageModel?) {
    }
}