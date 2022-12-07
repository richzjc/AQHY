package com.micker.seven.activity

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.kronos.router.BindRouter
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseRecyclerViewActivity
import com.micker.global.SEVEN_LIST_STAGE_ROUTER
import com.micker.seven.adapter.PoetryListAdapter
import com.micker.seven.db.RoomDataBaseInit
import com.micker.seven.db.SevenModelEnitity
import com.wallstreetcn.quotes.stocksearch.room.PoetryDbViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@BindRouter(urls = [SEVEN_LIST_STAGE_ROUTER])
class PoetryListActivity : BaseRecyclerViewActivity<SevenModelEnitity, Any, BasePresenter<Any>>() {

    override fun onRefresh() {
    }

    override fun onLoadMore(page: Int) {
    }

    override fun doInitAdapter() = PoetryListAdapter()

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        recycleView?.setIsEndless(false)
        ptrRecyclerView?.setCanRefresh(false)
    }

    override fun doInitData() {
        super.doInitData()
        CoroutineScope(SupervisorJob()).launch {
            adapter?.setData(RoomDataBaseInit.stockDao?.query())
        }
    }
}