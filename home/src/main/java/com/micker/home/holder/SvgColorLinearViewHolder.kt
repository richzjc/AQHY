package com.micker.home.holder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.data.model.home.ColorCategoryEntity
import com.micker.data.model.home.ColorTypeEntity
import com.micker.data.model.home.ColorsItem
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.home.utils.setColors
import kotlinx.android.synthetic.main.aqhy_recycler_item_svg_solid.view.*

class SvgColorLinearViewHolder(context: Context?) : BaseRecycleViewHolder<Any>(context) {
    override fun getLayoutId() = R.layout.aqhy_recycler_item_svg_solid

    override fun doBindData(content: Any?) {
        (content as? ColorCategoryEntity)?.apply {
            if (type == ColorTypeEntity.MaterialTypeLinearColor) {
                setLinear(this)
            } else if (type == ColorTypeEntity.MaterialTypeGradialColor) {
                setGradial(this)
            } else if (type == ColorTypeEntity.MaterialTypeSolidColor) {
                setSolid(this)
            }
            itemView?.name?.text = name
            itemView?.name?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }

        (content as? ColorsItem)?.apply {
            setColors(this, itemView.image)
            itemView?.name?.text = name
            itemView?.name?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }
    }

    private fun setSolid(colorCategoryEntity: ColorCategoryEntity) {
        val array = intArrayOf(Color.parseColor(colorCategoryEntity.color), Color.parseColor(colorCategoryEntity.color))
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        drawable.colors = array
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        itemView.image.setImageDrawable(drawable)
    }

    private fun setGradial(colorCategoryEntity: ColorCategoryEntity) {
        val array = IntArray(colorCategoryEntity.webColors?.size ?: 0)
        colorCategoryEntity?.webColors?.forEachIndexed { index, s ->
            array.set(index, Color.parseColor(s))
        }
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.colors = array
        drawable.gradientRadius = ScreenUtils.dip2px(25f).toFloat()
        drawable.gradientType = GradientDrawable.RADIAL_GRADIENT
        itemView.image.setImageDrawable(drawable)
    }

    private fun setLinear(colorCategoryEntity: ColorCategoryEntity) {
        val array = IntArray(colorCategoryEntity.webColors?.size ?: 0)
        colorCategoryEntity?.webColors?.forEachIndexed { index, s ->
            array.set(index, Color.parseColor(s))
        }
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        drawable.colors = array
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        itemView.image.setImageDrawable(drawable)
    }
}