package com.micker.user.activity

import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.data.constant.USER_MAIN_ABOUNT_ACTIVITY
import com.micker.helper.ResourceUtils
import com.micker.helper.system.EquipmentUtils
import com.micker.user.R
import kotlinx.android.synthetic.main.tail_activity_about.*

@BindRouter(urls = [USER_MAIN_ABOUNT_ACTIVITY])
class AboutActivity : BaseActivity<Any, BasePresenter<Any>>() {
    override fun doGetContentViewId() = R.layout.tail_activity_about

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        titlebar?.setBackgroundColor(ResourceUtils.getColor(R.color.color_f8f8f8))
        version?.text = "版本号： ${EquipmentUtils.getVersionName()}"
    }
}