package com.micker.five.holder

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.core.imageloader.ImageLoadManager
import com.micker.five.model.FiveStageModel
import com.micker.global.VIDEO_FULL_SCREEN_PLAY_ACTION
import com.micker.helper.router.RouterHelper
import com.micker.helper.rx.RxUtils
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.wallstreetcn.global.media.widget.FirstFrameUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.aqhy_recycler_item_five_stage.view.*

class FiveStageHolder(context: Context?) : BaseRecycleViewHolder<FiveStageModel>(context) {

    init {
        itemView?.image?.layoutParams?.also {
            it.width = ScreenUtils.dip2px(100f)
            it.height = ScreenUtils.dip2px(150f)
            itemView?.image?.layoutParams = it
            val hierarchy = itemView?.image?.hierarchy
            hierarchy?.setPlaceholderImage(R.drawable.default_img)
        }

        itemView?.setOnClickListener {
            if(super.content != null) {
                val bundle = Bundle()
                bundle.putString("videoUrl", super.content?.videoUrl)
                RouterHelper.open(VIDEO_FULL_SCREEN_PLAY_ACTION, mContext, bundle)
            }
        }
    }


    override fun getLayoutId() = R.layout.aqhy_recycler_item_five_stage

    override fun doBindData(content: FiveStageModel?) {
        super.content = content
        itemView?.title?.text = content?.title
        ImageLoadManager.loadImage(content?.imageUrl, itemView.image, 0)
    }
}