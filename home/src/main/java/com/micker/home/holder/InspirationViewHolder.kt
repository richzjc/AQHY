package com.micker.home.holder

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.core.imageloader.ImageLoadManager
import com.micker.data.constant.FIND_INSPIRATION_ACTION
import com.micker.data.constant.SVG_DETAIL_ACTIVITY
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.global.user.AccountManager.getAccountUserId
import com.micker.global.user.AccountManager.isLogin
import com.micker.helper.ResourceUtils
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.aqhy_recycler_item_home_category_list.view.*

class InspirationViewHolder(context: Context?, val isShowFindInspiration: Boolean) :
    BaseRecycleViewHolder<InspirationEntity>(context) {

    init {
        if (isShowFindInspiration) {
            itemView?.find_inspiration?.show()
            itemView?.find_inspiration_line?.show()
        } else {
            itemView?.find_inspiration?.hide(true)
            itemView?.find_inspiration_line?.hide(true)
        }

        itemView?.setOnClickListener {
            responseToItemClick()
        }

        itemView?.paint?.setOnClickListener {
            content?.apply {
                val bundle = Bundle()
                bundle.putParcelable("entity", svgItem)
                RouterHelper.open(SVG_DETAIL_ACTIVITY, mContext, bundle)
            }
        }

        itemView?.find_inspiration?.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("entity", content.svgItem)
            bundle.putString("svgId", content.svgId)
            RouterHelper.open(FIND_INSPIRATION_ACTION, mContext, bundle)
        }
    }

    private fun responseToItemClick() {
        if (isLogin() && (getAccountUserId() == content?.userId)) {
            content?.apply {
                val bundle = Bundle()
                bundle.putParcelable("entity", getNewSubItemEntity())
                RouterHelper.open(SVG_DETAIL_ACTIVITY, mContext, bundle)
            }
        } else {
            content?.apply {
                val bundle = Bundle()
                bundle.putParcelable("entity", svgItem)
                RouterHelper.open(SVG_DETAIL_ACTIVITY, mContext, bundle)
            }
        }
    }

    override fun getLayoutId() = R.layout.aqhy_recycler_item_home_category_list

    override fun doBindData(content: InspirationEntity?) {
        this.content = content

        content?.svgItem?.apply {
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

        if (isLogin() && (getAccountUserId() == content?.userId)) {
            itemView?.delete?.text = ResourceUtils.getResStringFromId(R.string.icon_more)
            itemView?.delete?.setTag(R.id.tag_isshare, false)
            itemView?.delete?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        } else {
            itemView?.delete?.text = "分享"
            itemView?.delete?.setTag(R.id.tag_isshare, true)
            itemView?.delete?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }
    }
}