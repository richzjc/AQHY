package com.micker.home.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micker.data.model.home.ColorCategoryEntity
import com.micker.data.model.home.ColorsItem
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.home.adapter.SvgColorHistoryAdapter
import com.micker.home.singleton.ColorHistoryFactory
import kotlinx.android.synthetic.main.aqhy_view_svg_detail_footer_history.view.*

class SvgDetailFooterHistoryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val adapter by lazy { SvgColorHistoryAdapter() }

    init {
        orientation = LinearLayout.VERTICAL
        LayoutInflater.from(getContext()).inflate(R.layout.aqhy_view_svg_detail_footer_history, this, true)
        rv?.setLayoutManager(LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false))
        rv.addItemDecoration(SpaceItemDecoration())
        rv.setIsEndless(false)
        rv.setEmptyImageRes(0)
        rv.adapter = adapter
        setListener()
    }

    private fun setListener() {
        close?.setOnClickListener {
            (parent?.parent as? SvgDetailFooterView)?.showOuterView()
        }

        adapter?.setAdapterItemClickListener { view, entity, position ->
            (parent?.parent as? SvgDetailFooterView)?.showOuterView()
            if (entity is ColorCategoryEntity) {
                (parent?.parent as? SvgDetailFooterView)?.updateCategoryData(
                    (entity as? ColorCategoryEntity)?.type?.type ?: 0
                )
            } else if (entity is ColorsItem) {
                (parent?.parent as? SvgDetailFooterView)?.updateCategoryData((entity as? ColorsItem)?.type ?: 0)
            }
        }
    }

    fun bindColorCategoryData(colorCategoryList: ArrayList<ColorCategoryEntity>?) {
        adapter?.setData(colorCategoryList as? List<Any>)
    }

    fun bindHistoryData() {
        adapter?.setData(ColorHistoryFactory.historyData as? List<Any>)
    }

    private class SpaceItemDecoration : RecyclerView.ItemDecoration() {
        private val padding by lazy {
            val screenWidth = ScreenUtils.getScreenWidth()
            val totalViewWidth = ScreenUtils.dip2px(250f)
            val totalPadding = screenWidth - totalViewWidth
            totalPadding / 10
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(padding, 0, padding, 0)
        }
    }
}