package com.micker.home.holder

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.micker.core.imageloader.ImageLoadManager
import com.micker.core.imageloader.WscnImageView
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.global.callback.ISharePoster
import com.micker.global.const.SHARE_BOTH
import com.micker.global.const.SHARE_BOTTOM
import com.micker.global.const.SHARE_NONE
import com.micker.global.const.SHARE_TOP
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show

class SvgPosterViewHolder : ISharePoster {

    private var contentView: View? = null

    override fun initView(parent: ViewGroup) {
        contentView =
            LayoutInflater.from(parent.context).inflate(R.layout.aqhy_view_svg_share, parent, true)
        val type = SharedPrefsUtil.getInt("shareSetting", SHARE_BOTH)
        val topView = contentView?.findViewById<View>(R.id.top)
        val bgView = contentView?.findViewById<View>(R.id.bg)
        val params = bgView?.layoutParams as? LinearLayout.LayoutParams
        if (type == SHARE_BOTH || type == SHARE_TOP) {
            topView?.show()
            params?.topMargin = -(ScreenUtils.dip2px(18f))
            bgView?.layoutParams = params
        } else {
            topView?.hide(true)
            params?.topMargin = 0
            bgView?.layoutParams = params
        }
    }

    override fun bindData(parcel: Parcelable?) {
        contentView?.postDelayed({
            Log.i("width", "${contentView!!.width}")
            (parcel as? InspirationEntity)?.apply {
                updateView(
                    contentView,
                    contentView!!.width,
                    (contentView!!.width * svgItem!!.imgHeight) / svgItem!!.imgWidth
                )
            }
        }, 500)
    }

    private fun InspirationEntity.updateView(shareView: View?, width: Int, height: Int) {
        val bg = shareView?.findViewById<WscnImageView>(R.id.bg)
        val params = bg?.layoutParams
        params?.width = width
        params?.height = height
        bg?.layoutParams = params
        setBitmap(image!!, bg)

        val type = SharedPrefsUtil.getInt("shareSetting", SHARE_BOTH)
        val bottomView = contentView?.findViewById<View>(R.id.bottom_parent)
        if (type == SHARE_BOTH || type == SHARE_BOTTOM) {
            bottomView?.show()
        } else {
            bottomView?.hide(true)
        }
    }

    override fun getView() = contentView

    private fun setBitmap(path: String, image: WscnImageView?) {

        val type = SharedPrefsUtil.getInt("shareSetting", SHARE_BOTH)
        if (type == SHARE_NONE) {
            ImageLoadManager.loadRoundImage(
                path,
                image,
                0,
                0,
                0,
                0,
                0
            )
        } else {
            ImageLoadManager.loadRoundImage(
                path,
                image,
                0,
                ScreenUtils.dip2px(15f),
                ScreenUtils.dip2px(15f),
                0,
                0
            )
        }
    }
}