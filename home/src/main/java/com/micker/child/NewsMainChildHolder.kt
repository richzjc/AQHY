package com.micker.child

import com.micker.core.adapter.BaseRecycleViewHolder
import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import com.micker.core.imageloader.ImageLoadManager
import com.micker.core.imageloader.ProgressResponseListener
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_recycler_item_news_main_child.view.*

class NewsMainChildHolder(context: Context?) : BaseRecycleViewHolder<NewsMainChildEntity>(context) {
    init {
        itemView?.setOnClickListener {
            if (!TextUtils.isEmpty(super.content?.router))
                RouterHelper.open(super.content.router, mContext)
        }
    }

    override fun getLayoutId() = R.layout.aqhy_recycler_item_news_main_child

    override fun doBindData(content: NewsMainChildEntity?) {
        super.content = content
        if (!TextUtils.isEmpty(content?.imageUrl)) {
            if (super.content.ratio <= 0) {
                ImageLoadManager.loadBitmap(super.content.imageUrl, object : ProgressResponseListener<Bitmap>{
                    override fun onComplete(source: Bitmap?) {
                        if(source != null){
                            val ratio = source.width * 1f/source.height
                            content?.ratio = ratio
                            itemView?.image?.aspectRatio = ratio
                            ImageLoadManager.loadRoundImage(content?.imageUrl, itemView.image, R.drawable.default_img, ScreenUtils.dip2px(5f))
                        }
                    }
                })
            } else {
                itemView?.image?.aspectRatio = super.content.ratio
                ImageLoadManager.loadRoundImage(super.content.imageUrl, itemView.image, R.drawable.default_img, ScreenUtils.dip2px(5f))
            }
        }

        itemView?.title?.text = content?.title
        itemView?.stage?.text = "${content?.stage}"
        itemView?.desc?.text = content?.desc
    }
}