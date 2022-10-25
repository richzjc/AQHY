package com.micker.five.activity

import android.view.View
import com.alibaba.fastjson.JSON
import com.kronos.router.BindRouter
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseRecyclerViewActivity
import com.micker.five.adapter.FiveStageAdapter
import com.micker.five.model.FiveStageModel
import com.micker.global.FIVE_STAGE_ROUTER
import com.micker.helper.file.CacheUtils

@BindRouter(urls = [FIVE_STAGE_ROUTER])
class FiveStageActivity : BaseRecyclerViewActivity<FiveStageModel, Any, BasePresenter<Any>>(){

    override fun onRefresh() {
    }

    override fun onLoadMore(page: Int) {
    }

    override fun doInitAdapter() = FiveStageAdapter()

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        titleBar?.title = "家庭教育"
        ptrRecyclerView?.setCanRefresh(false)
        recycleView?.setIsEndless(false)
    }

    override fun doInitData() {
        super.doInitData()
        val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets("five_stage.json"))
        val list = JSON.parseArray(json, FiveStageModel::class.java)
        adapter?.setData(list)
    }
}