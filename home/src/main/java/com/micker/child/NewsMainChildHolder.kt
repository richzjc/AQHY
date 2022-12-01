package com.micker.child

import com.micker.core.adapter.BaseRecycleViewHolder
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import com.micker.core.imageloader.ImageLoadManager
import com.micker.core.imageloader.ProgressResponseListener
import com.micker.helper.ReflectionUtils
import com.micker.helper.ResourceUtils
import com.micker.helper.image.ImageUtlFormatHelper
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
        if (!TextUtils.isEmpty(content?.imageUrl) && content!!.imageUrl.startsWith("http")) {
            val imageUrl = ImageUtlFormatHelper.formatImageFactory(content?.imageUrl, ScreenUtils.getScreenWidth()/2, 0)
            if (super.content.ratio <= 0) {
                ImageLoadManager.loadBitmap(imageUrl, object : ProgressResponseListener<Bitmap>{
                    override fun onComplete(source: Bitmap?) {
                        if(source != null){
                            val ratio = source.width * 1f/source.height
                            content?.ratio = ratio
                            itemView?.image?.aspectRatio = ratio
                            ImageLoadManager.loadRoundImage(imageUrl, itemView.image, R.drawable.default_img, ScreenUtils.dip2px(5f))
                        }
                    }
                })
            }else {
                itemView?.image?.aspectRatio = super.content.ratio
                ImageLoadManager.loadRoundImage(imageUrl, itemView.image, R.drawable.default_img, ScreenUtils.dip2px(5f))
            }
        } else if(!TextUtils.isEmpty(content?.imageUrl)){
            val id = ResourceUtils.getDrawableId(mContext, content?.imageUrl)
            val drawable = ResourceUtils.getResDrawableFromID(id)
            val ratio = drawable.intrinsicWidth * 1f/drawable.intrinsicHeight
            itemView?.image?.aspectRatio = ratio
            ImageLoadManager.loadImage(id, itemView.image, 0)
        }

        itemView?.title?.text = content?.title
        itemView?.stage?.text = "${content?.stage}"
        itemView?.desc?.text = content?.desc
    }
}