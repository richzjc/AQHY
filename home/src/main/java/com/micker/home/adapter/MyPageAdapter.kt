package com.micker.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.micker.data.model.home.ColorGroupByTypeEntity
import com.micker.data.model.home.ColorsItem
import com.micker.home.singleton.ColorsItemFactory
import com.micker.home.widget.list.ColorListView

class MyPageAdapter : PagerAdapter() {

    var mdatas: List<ColorGroupByTypeEntity>? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val colorListView = ColorListView(container.context)
        container.addView(colorListView)
        val adapter by lazy {
            val adapter = ColorListAdapter()
            adapter.itemClickCallback = object : ColorListAdapter.ItemClickCallback {
                override fun onItemClick(colorsItem: ColorsItem) {
                    ColorsItemFactory.selectColorsItem(colorsItem)
                }
            }
            adapter
        }
        adapter.setData(mdatas?.get(position)?.colors)
        colorListView?.adapter = adapter
        return colorListView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount() = mdatas?.size ?: 0

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}