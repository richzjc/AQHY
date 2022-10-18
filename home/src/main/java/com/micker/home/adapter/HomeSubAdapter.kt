package com.micker.home.adapter

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.helper.system.ScreenUtils
import com.micker.home.holder.HomeSubViewHolder

class HomeSubAdapter : BaseRecycleAdapter<HomeSubItemEntity, HomeSubViewHolder>() {
    override fun binderItemHolder(holder: HomeSubViewHolder?, position: Int) {
        updateItemWidth(holder, position)
        holder?.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): HomeSubViewHolder {
        return HomeSubViewHolder(parent?.context)
    }

    private fun updateItemWidth(holder: HomeSubViewHolder?, position: Int) {
        holder?.apply {
            val params = itemView.layoutParams as? ViewGroup.MarginLayoutParams
            params?.leftMargin = ScreenUtils.dip2px(10f)
            if(position == listItemCount - 1){
                params?.rightMargin = ScreenUtils.dip2px(10f)
            }
            itemView.layoutParams = params
        }
    }
}