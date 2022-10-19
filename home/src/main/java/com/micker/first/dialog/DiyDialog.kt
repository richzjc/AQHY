package com.micker.first.dialog

import android.view.Gravity
import android.view.WindowManager
import com.micker.core.base.BaseDialogFragment
import com.micker.first.callback.DiyCallback
import com.micker.helper.system.ScreenUtils
import com.micker.home.R

class DiyDialog : BaseDialogFragment() {
    var diyCallback: DiyCallback? = null


    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    override fun getStyle(): Int {
        return R.style.DefaultDialog
    }


    override fun getDialogWidth(): Int {
        return ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(100f)
    }

    override fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun doGetContentViewId() = R.layout.aqhy_dialog_first_stage_diy

    override fun doInitData() {
        super.doInitData()

    }
}