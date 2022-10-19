package com.micker.first.dialog

import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.micker.core.base.BaseDialogFragment
import com.micker.first.callback.DiyCallback
import com.micker.global.util.ShapeDrawable
import com.micker.helper.ResourceUtils
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_dialog_first_stage_diy.*

class DiyDialog : BaseDialogFragment() {

    private val drawable by lazy {
        val color = ResourceUtils.getColor(R.color.color_1482f0)
        ShapeDrawable.getDrawable(0, ScreenUtils.dip2px(5f), color, color)
    }

    var diyCallback: DiyCallback? = null


    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    override fun getStyle(): Int {
        return R.style.DefaultDialog
    }


    override fun getDialogWidth(): Int {
        return ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(50f)
    }

    override fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun doGetContentViewId() = R.layout.aqhy_dialog_first_stage_diy


    override fun doInitData() {
        super.doInitData()
        confirm?.background = drawable
        setListener()
    }

    private fun setListener(){
        confirm?.setOnClickListener {

        }
    }
}