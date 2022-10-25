package com.micker.five.adapter

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.five.holder.FiveStageHolder
import com.micker.five.model.FiveStageModel

class FiveStageAdapter : BaseRecycleAdapter<FiveStageModel, FiveStageHolder>() {
    override fun binderItemHolder(holder: FiveStageHolder?, position: Int) {
       holder?.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): FiveStageHolder {
        return FiveStageHolder(parent?.context)
    }
}