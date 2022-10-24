package com.micker.four.dialog

import android.graphics.Bitmap
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.micker.core.base.BaseDialogFragment
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_dialog_four_stage_preview.*

class PreviewDialog : BaseDialogFragment() {

    var originBitmap: Bitmap? = null

    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    override fun getStyle(): Int {
        return R.style.DefaultDialog
    }

    override fun getDialogWidth(): Int {
        if (originBitmap != null) {
            var widthSize1 = ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(50f)
            val heightSize = ScreenUtils.getScreenHeight() - ScreenUtils.dip2px(150f)
            val tempHeight = (originBitmap!!.height * 1f / originBitmap!!.width) * widthSize1

            var realHeightSize: Int
            var widthSize: Int


            if (tempHeight > heightSize) {
                realHeightSize = heightSize
                widthSize =
                    ((originBitmap!!.width * 1f / originBitmap!!.height) * realHeightSize).toInt()
            } else {
                widthSize = widthSize1
                realHeightSize =
                    ((originBitmap!!.height * 1f / originBitmap!!.width) * widthSize1).toInt()
            }

            return widthSize
        } else {
            return WindowManager.LayoutParams.WRAP_CONTENT
        }
        return ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(50f)
    }

    override fun getDialogHeight(): Int {
        if (originBitmap != null) {

            var widthSize1 = ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(50f)
            val heightSize = ScreenUtils.getScreenHeight() - ScreenUtils.dip2px(150f)
            val tempHeight = (originBitmap!!.height * 1f / originBitmap!!.width) * widthSize1

            var realHeightSize: Int
            var widthSize: Int


            if (tempHeight > heightSize) {
                realHeightSize = heightSize
                widthSize =
                    ((originBitmap!!.width * 1f / originBitmap!!.height) * realHeightSize).toInt()
            } else {
                widthSize = widthSize1
                realHeightSize =
                    ((originBitmap!!.height * 1f / originBitmap!!.width) * widthSize1).toInt()
            }

            return realHeightSize
        } else {
            return WindowManager.LayoutParams.WRAP_CONTENT
        }
    }

    override fun doGetContentViewId() = R.layout.aqhy_dialog_four_stage_preview


    override fun doInitData() {
        super.doInitData()
        if(originBitmap != null)
            img?.setImageBitmap(originBitmap)
    }

}