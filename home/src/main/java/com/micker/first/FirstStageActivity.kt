package com.micker.first

import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.global.FIRST_STAGE_ROUTER
import com.micker.home.R

@BindRouter(urls = [FIRST_STAGE_ROUTER])
class FirstStageActivity : BaseActivity<Any, BasePresenter<Any>>() {

    override fun doGetContentViewId() = R.layout.aqhy_activity_first_stage
}