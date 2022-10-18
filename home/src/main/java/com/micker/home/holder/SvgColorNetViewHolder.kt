package com.micker.home.holder

import android.content.Context
import android.util.TypedValue
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.core.imageloader.ImageLoadManager
import com.micker.data.model.home.ColorCategoryEntity
import com.micker.data.model.home.ColorsItem
import com.micker.home.R
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.aqhy_recycler_item_svg_solid.view.*

class SvgColorNetViewHolder(context : Context?) : BaseRecycleViewHolder<Any>(context) {
    override fun getLayoutId() = R.layout.aqhy_recycler_item_svg_solid

    override fun doBindData(content: Any?) {
        itemView?.image?.show()
        if(content is ColorCategoryEntity){
            ImageLoadManager.loadImage(content.image, itemView?.image, 0)
            itemView?.name?.text = (content as? ColorCategoryEntity)?.name
            itemView?.name?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }else if(content is ColorsItem){
            ImageLoadManager.loadImage(content.image, itemView?.image, 0)
            itemView?.name?.text = (content as? ColorsItem)?.name
            itemView?.name?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }
    }
}