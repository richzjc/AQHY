package com.micker.home.holder

import android.content.Context
import android.os.Bundle
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.core.imageloader.ImageLoadManager
import com.micker.data.constant.SVG_DETAIL_ACTIVITY
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_recycler_item_home_sub.view.*

class HomeSubViewHolder(context : Context?) : BaseRecycleViewHolder<HomeSubItemEntity>(context) {

    init {
        itemView?.setOnClickListener {
            content?.apply {
                val bundle = Bundle()
                bundle.putParcelable("entity", this)
                RouterHelper.open(SVG_DETAIL_ACTIVITY, mContext, bundle)
            }
        }
    }

    override fun getLayoutId() = R.layout.aqhy_recycler_item_home_sub

    override fun doBindData(content: HomeSubItemEntity?) {
        this.content = content
        val lp = itemView.image.layoutParams
        val height = ScreenUtils.dip2px(200f)
        val widht = (content!!.imgWidth * height)/content!!.imgHeight
        lp.width = widht
        lp.height = height
        itemView.image.layoutParams = lp
        ImageLoadManager.loadImage(content.image, itemView.image, 0)
    }
}