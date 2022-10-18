package com.micker.home.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.micker.core.imageloader.ImageLoadManager
import com.micker.core.imageloader.WscnImageView
import com.micker.data.model.home.ColorTypeEntity
import com.micker.data.model.home.ColorsItem
import com.micker.helper.system.ScreenUtils

fun setColors(content : ColorsItem?, image : WscnImageView){
    content?.apply {
        if (type == ColorTypeEntity.MaterialTypeLinearColor.type) {
            setLinear(this, image)
        } else if (type == ColorTypeEntity.MaterialTypeGradialColor.type) {
            setGradial(this, image)
        }else if(type == ColorTypeEntity.MaterialTypeSolidColor.type){
            setSolid(this, image)
        }else{
            ImageLoadManager.loadImage(content.image, image, 0)
        }
    }
}

private fun setSolid(colorsItem: ColorsItem, image : WscnImageView) {
    val list = colorsItem.color!!.split(",")
    if (list.isNotEmpty()) {
        val array = IntArray(2)
        array[0] = Color.parseColor("#${list[0]}")
        array[1] = Color.parseColor("#${list[0]}")
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        drawable.colors = array
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        image.setImageDrawable(drawable)
    }
}

private fun setGradial(colorsItem: ColorsItem, image : WscnImageView) {
    val list = colorsItem.color!!.split(",")
    val array = IntArray(list.size)
    list.forEachIndexed { index, s ->
        array.set(index, Color.parseColor("#${s}"))
    }
    val drawable = GradientDrawable()
    drawable.shape = GradientDrawable.OVAL
    drawable.colors = array
    drawable.gradientRadius = ScreenUtils.dip2px(25f).toFloat()
    drawable.gradientType = GradientDrawable.RADIAL_GRADIENT
    image.setImageDrawable(drawable)
}

private fun setLinear(colorsItem: ColorsItem, image : WscnImageView) {
    val list = colorsItem.color!!.split(",")
    val array = IntArray(list.size)
    list.forEachIndexed { index, s ->
        array.set(index, Color.parseColor("#${s}"))
    }
    val drawable = GradientDrawable()
    drawable.shape = GradientDrawable.OVAL
    drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
    drawable.colors = array
    drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    image.setImageDrawable(drawable)
}