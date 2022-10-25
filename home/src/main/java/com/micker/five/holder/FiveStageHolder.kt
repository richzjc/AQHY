package com.micker.five.holder

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.micker.core.adapter.BaseRecycleViewHolder
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
            it.height = ScreenUtils.dip2px(100f)
            itemView?.image?.layoutParams = it
            val hierarchy = itemView?.image?.hierarchy
            hierarchy?.setPlaceholderImage(R.drawable.default_img)
        }

        itemView?.setOnClickListener {
            if(super.content != null) {
                val bundle = Bundle()
                bundle.putString("url", super.content?.url)
                RouterHelper.open(VIDEO_FULL_SCREEN_PLAY_ACTION, mContext, bundle)
            }
        }
    }


    override fun getLayoutId() = R.layout.aqhy_recycler_item_five_stage

    override fun doBindData(content: FiveStageModel?) {
        super.content = content
        itemView?.title?.text = content?.title
        showFirstFrame(content?.url)
    }


    fun showFirstFrame(url: String?) {
        RxUtils.just(url)
            .map { s -> FirstFrameUtils.getFirstFrame(s) }
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(RxUtils.getSchedulerIo())
            .subscribe(Consumer<Bitmap?> { t ->
                if (t != null) {
                    val realHeight = (t.height * 1f / t.width) * ScreenUtils.dip2px(100f)
                    var params = itemView?.image?.layoutParams as? LinearLayout.LayoutParams
                    if (params == null)
                        params =
                            LinearLayout.LayoutParams(ScreenUtils.dip2px(100f), realHeight.toInt())
                    params.width = ScreenUtils.dip2px(100f)
                    params.height = realHeight.toInt()
                    itemView?.image?.layoutParams = params
                    itemView?.image?.setImageBitmap(t)
                }
            }, object : Consumer<Throwable?> {
                override fun accept(t: Throwable?) {
                    Log.i("@@@", t?.message ?: "")
                }
            })
    }
}