package com.micker.first

import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.micker.core.base.BasePresenter
import com.micker.core.base.BaseRecyclerViewActivity
import com.micker.first.adapter.ChildWordAdapter
import com.micker.first.model.ChineseWordModel
import com.micker.helper.file.CacheUtils
import com.micker.helper.system.ScreenUtils
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration

class ChineseWordListActivity :
    BaseRecyclerViewActivity<ChineseWordModel, Any, BasePresenter<Any>>() {

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        recycleView.setIsEndless(false)
        ptrRecyclerView?.setCanRefresh(false)
        recycleView.setLayoutManager(StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL))
        recycleView?.addItemDecoration(GridItemOffset())
        titleBar?.title = "文字列表"
        val padding = ScreenUtils.dip2px(10f)
        recycleView.setPadding(padding, padding, padding, padding)
    }


    override fun onRefresh() {
    }

    override fun onLoadMore(page: Int) {
    }

    override fun doInitAdapter() = ChildWordAdapter()

    override fun doInitData() {
        super.doInitData()
        val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets("first_stage.json"))
        val jobj = org.json.JSONObject(json)
        val keys = jobj.keys()
        val list = ArrayList<ChineseWordModel>()
        keys.forEach {
            val value = jobj.optString(it, "")
            val charArr = value.toCharArray()
            val bihua = it.toInt()
            val lenght = value.length
            (0 until lenght)?.forEach {
                val str = charArr[it].toString()
                if (!TextUtils.equals(str, "\\") && !TextUtils.equals("n", str) && !TextUtils.equals("\n", str))
                    list.add(ChineseWordModel(bihua, str))
            }
        }
        adapter?.setData(list)
    }


    class GridItemOffset() : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val itemPosition = parent.getChildAdapterPosition(view)
            if (itemPosition == RecyclerView.NO_POSITION) {
                return
            }



            if(itemPosition%8 == 7)
                outRect.right = 0

            outRect.bottom = ScreenUtils.dip2px(10f)
        }
    }
}