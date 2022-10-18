package com.micker.home.adapter

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.data.model.home.ColorCategoryEntity
import com.micker.data.model.home.ColorTypeEntity
import com.micker.data.model.home.ColorsItem
import com.micker.home.holder.SvgColorLinearViewHolder
import com.micker.home.holder.SvgColorNetViewHolder

class SvgColorHistoryAdapter : BaseRecycleAdapter<Any, BaseRecycleViewHolder<Any>>() {

    override fun binderItemHolder(holder: BaseRecycleViewHolder<Any>?, position: Int) {
        holder?.doBindData(get(position))
        holder?.itemView?.setOnClickListener {
            if(holder != null)
                onAdapterItemClick(it, holder)
        }
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): BaseRecycleViewHolder<Any> {
       return when(viewType){
            solidColor.type -> SvgColorLinearViewHolder(parent?.context)
            linearColor.type -> SvgColorLinearViewHolder(parent?.context)
            gradialColor.type -> SvgColorLinearViewHolder(parent?.context)
            netImage -> SvgColorNetViewHolder(parent?.context)
            else -> SvgColorNetViewHolder(parent?.context)
        }
    }

    override fun getListType(listItemPosition: Int): Int {
        val entity = get(listItemPosition)
        if(entity is ColorCategoryEntity){
            if(entity.type == ColorTypeEntity.MaterialTypeSolidColor)
                return solidColor.type
            else if(entity.type == ColorTypeEntity.MaterialTypeLinearColor)
                return linearColor.type
            else if(entity.type == ColorTypeEntity.MaterialTypeGradialColor)
                return gradialColor.type
            else return netImage
        }else if(entity is ColorsItem){
            if(entity.type == ColorTypeEntity.MaterialTypeSolidColor.type)
                return solidColor.type
            else if(entity.type == ColorTypeEntity.MaterialTypeLinearColor.type)
                return linearColor.type
            else if(entity.type == ColorTypeEntity.MaterialTypeGradialColor.type)
                return gradialColor.type
            else return netImage
        }
        return super.getListType(listItemPosition)
    }

    companion object {
        val solidColor = ColorTypeEntity.MaterialTypeSolidColor
        val linearColor = ColorTypeEntity.MaterialTypeLinearColor
        val gradialColor = ColorTypeEntity.MaterialTypeGradialColor
        val netImage = 30
    }
}