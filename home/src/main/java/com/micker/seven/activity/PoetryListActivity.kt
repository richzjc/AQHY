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
        val stream = CacheUtils.getFileFromAssets("seven/poetry_database.json")
        val json = CacheUtils.InputStreamToString(stream)
        val jarr = JSONArray(json)
        val length = jarr.length()
        val list = ArrayList<SevenModelEnitity>()
        (0 until length)?.forEach {
            val entity = SevenModelEnitity()
            val jobj = jarr.optJSONArray(it)
            entity.title = jobj.opt(0).toString()
            entity.author = jobj.opt(1).toString()
            entity.content = jobj.opt(2).toString()
            entity.tranlate = jobj.opt(3).toString()
            entity.id = jobj.opt(4).toString().toInt()
            list.add(entity)
        }
        adapter?.setData(list)

    }
}