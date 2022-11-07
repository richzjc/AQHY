package com.micker.six.activity

import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.global.SIX_STAGE_ROUTER
import com.micker.home.R

@BindRouter(urls = [SIX_STAGE_ROUTER])
class SixActivity : BaseActivity<Any, BasePresenter<Any>>() {

    override fun doGetContentViewId() = R.layout.aqhy_activity_six_stage


}