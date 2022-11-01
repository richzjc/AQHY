package com.micker.five.activity

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseRecyclerViewFragment
import com.micker.core.widget.DividerItemDecoration
import com.micker.five.adapter.FiveStageAdapter
import com.micker.five.model.FiveStageModel
import com.micker.helper.file.CacheUtils
import com.micker.helper.system.ScreenUtils

class FiveStageFragment : BaseRecyclerViewFragment<FiveStageModel, Any, BasePresenter<Any>>(){

    override fun onRefresh() {
    }

    override fun onLoadMore(page: Int) {
    }

    override fun doInitAdapter() = FiveStageAdapter()

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        ptrRecyclerView?.setCanRefresh(false)
        recycleView?.setIsEndless(false)

        val itemDecoration = DividerItemDecoration(LinearLayoutManager.VERTICAL, ScreenUtils.dip2px(1f), Color.parseColor("#eeeeee"), 0)
        recycleView?.addItemDecoration(itemDecoration)
    }

    override fun doInitData() {
        super.doInitData()
        val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets("five_stage.json"))
        val list = JSON.parseArray(json, FiveStageModel::class.java)
        adapter?.setData(list)
    }
}