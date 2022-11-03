package com.micker.child

import android.view.View
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.core.base.BaseFragment
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseWaterfallFragment
import com.micker.home.R

class NewsMainChildFragment : BaseWaterfallFragment<NewsMainChildEntity, Any, BasePresenter<Any>>(){

    override fun doInitSubViews(view: View?) {
        super.doInitSubViews(view)
        titleBar?.visibility = View.GONE
        ptrRecyclerView?.setCanRefresh(false)
        ptrRecyclerView?.isEnableLoadMore = false
        recycleView?.setIsEndless(false)
    }
    override fun onRefresh() {
    }

    override fun onLoadMore(page: Int) {
    }

    override fun doInitAdapter() = NewsMainChildAdapter()

    override fun doInitData() {
        super.doInitData()
    }
}