package com.micker.seven.activity

import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseRecyclerViewActivity
import com.micker.seven.model.SevenModel

class PoetryListActivity : BaseRecyclerViewActivity<SevenModel, Any, BasePresenter<Any>>() {
    override fun onRefresh() {
        TODO("Not yet implemented")
    }

    override fun onLoadMore(page: Int) {
        TODO("Not yet implemented")
    }

    override fun doInitAdapter(): BaseRecycleAdapter<*, *> {
        TODO("Not yet implemented")
    }
}