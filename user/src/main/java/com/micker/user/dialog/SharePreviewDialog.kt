package com.micker.user.dialog

import android.view.Gravity
import com.micker.core.base.BaseDialogFragment
import com.micker.helper.ResourceUtils
import com.micker.helper.system.ScreenUtils
import com.micker.user.R
import kotlinx.android.synthetic.main.aqhy_dialog_share_preview.*

class SharePreviewDialog : BaseDialogFragment() {
    override fun getStyle(): Int {
        return R.style.DefaultDialog
    }

    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    override fun doGetContentViewId(): Int {
        return R.layout.aqhy_dialog_share_preview
    }

    override fun doInitData() {
        val id = arguments?.getInt("drawableId") ?: 0
        preview?.setImageResource(id)
    }


    override fun getDialogWidth(): Int {
        return ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(30f)
    }

    override fun getDialogHeight(): Int {
        try {
            val id = arguments?.getInt("drawableId") ?: 0
            val drawable = ResourceUtils.getResDrawableFromID(id)
            val height = (dialogWidth * drawable.intrinsicHeight)/drawable.intrinsicWidth
            return height
        } catch (e: Exception) {
            e.printStackTrace()
            return super.getDialogHeight()
        }
    }
}