package com.micker.child

import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSON
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.core.base.BaseFragment
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseWaterfallFragment
import com.micker.helper.file.CacheUtils
import com.micker.home.R

class NewsMainChildFragment :
    BaseWaterfallFragment<NewsMainChildEntity, Any, BasePresenter<Any>>() {

    private var list: List<NewsMainChildEntity>? = null
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
        adapter?.setData(null)
        loadData()
    }

    private fun loadData() {
        if (list != null && list!!.isNotEmpty()) {
            adapter?.setData(list)
        } else {
            val json = arguments?.getString("json", "")
            if (!TextUtils.isEmpty(json)) {
                val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets(json))
                list = JSON.parseArray(json, NewsMainChildEntity::class.java)
                adapter?.setData(list)
            } else {
                adapter.setData(ArrayList<NewsMainChildEntity>())
            }
        }
    }
}