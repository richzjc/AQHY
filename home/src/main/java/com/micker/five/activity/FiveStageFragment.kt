package com.micker.five.activity

import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseRecyclerViewFragment
import com.micker.core.widget.DividerItemDecoration
import com.micker.five.adapter.FiveStageAdapter
import com.micker.five.api.FiveApi
import com.micker.five.model.FiveStageModel
import com.micker.helper.file.CacheUtils
import com.micker.helper.system.ScreenUtils
import com.micker.rpc.ResponseListener
import com.wallstreetcn.rpc.coroutines.getDeffer
import com.wallstreetcn.rpc.coroutines.requestData

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

        val api = FiveApi(arguments, object : ResponseListener<String>{
            override fun onSuccess(data: String?, isCache: Boolean) {
                if(!TextUtils.isEmpty(data)){
                    adapter?.setData(JSON.parseArray(data, FiveStageModel::class.java))
                }else{
                    adapter?.setData(ArrayList<FiveStageModel>())
                }
            }

            override fun onErrorResponse(code: Int, error: String?) {
                Log.e("code", "code = ${code}")
            }
        })
        api.start()
    }
}