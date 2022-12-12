package com.micker.seven.activity

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.router.BindRouter
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseRecyclerViewActivity
import com.micker.core.widget.DividerItemDecoration
import com.micker.global.SEVEN_LIST_STAGE_ROUTER
import com.micker.helper.ResourceUtils
import com.micker.helper.file.CacheUtils
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.seven.adapter.PoetryListAdapter
import com.micker.seven.const.list
import com.micker.seven.model.SevenModelEnitity
import org.json.JSONArray

@BindRouter(urls = [SEVEN_LIST_STAGE_ROUTER])
class PoetryListActivity : BaseRecyclerViewActivity<SevenModelEnitity, Any, BasePresenter<Any>>() {

    override fun onRefresh() {
    }

    override fun onLoadMore(page: Int) {
    }

    override fun doInitAdapter() = PoetryListAdapter()

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        titleBar?.title = "诗词天地"
        recycleView?.setIsEndless(false)
        ptrRecyclerView?.setCanRefresh(false)
        recycleView?.setBackgroundColor(Color.WHITE)
        val itemDecoration = DividerItemDecoration(LinearLayoutManager.VERTICAL, ScreenUtils.dip2px(0.5f), ResourceUtils.getColor(
            R.color.day_mode_divide_line_color_e6e6e6), 0)
        recycleView?.addItemDecoration(itemDecoration)
    }

    override fun doInitData() {
        super.doInitData()
        adapter?.setData(list)

    }
}