package com.micker.home.holder

import android.content.Context
import android.os.Bundle
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.core.imageloader.ImageLoadManager
import com.micker.data.constant.FIND_INSPIRATION_ACTION
import com.micker.data.constant.SVG_DETAIL_ACTIVITY
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.pawegio.kandroid.hide
import kotlinx.android.synthetic.main.aqhy_recycler_item_home_category_list.view.*

class HomeCategoryListViewHolder(context: Context?) : BaseRecycleViewHolder<HomeSubItemEntity>(context) {

    init {
        itemView?.setOnClickListener {
            content?.apply {
                val bundle = Bundle()
                bundle.putParcelable("entity", this)
                RouterHelper.open(SVG_DETAIL_ACTIVITY, mContext, bundle)
            }
        }

        itemView?.paint?.setOnClickListener {
            content?.apply {
                val bundle = Bundle()
                bundle.putParcelable("entity", this)
                RouterHelper.open(SVG_DETAIL_ACTIVITY, mContext, bundle)
            }
        }

        itemView?.find_inspiration?.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("entity", content)
            bundle.putString("svgId", content.id)
            RouterHelper.open(FIND_INSPIRATION_ACTION, mContext, bundle)
        }

        itemView?.delete?.hide(true)
        itemView?.delete_line?.hide(true)
    }

    override fun getLayoutId() = R.layout.aqhy_recycler_item_home_category_list

    override fun doBindData(content: HomeSubItemEntity?) {
        this.content = content
        content?.apply {
            itemView?.image?.aspectRatio = imgWidth.toFloat() / imgHeight
            ImageLoadManager.loadRoundImage(
                content.image,
                itemView.image,
                0,
                ScreenUtils.dip2px(10f),
                ScreenUtils.dip2px(10f),
                0,
                0
            )
        }
    }
}