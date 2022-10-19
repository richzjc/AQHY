package com.micker.first

import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.global.FIRST_STAGE_ROUTER
import com.micker.global.const.imagesArry
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_activity_first_stage.*
import kotlin.random.Random

@BindRouter(urls = [FIRST_STAGE_ROUTER])
class FirstStageActivity : BaseActivity<Any, BasePresenter<Any>>() {

    override fun doGetContentViewId() = R.layout.aqhy_activity_first_stage

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        initBg()
    }

    private fun initBg(){
        val size = imagesArry.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadImage(imagesArry[index], bg, 0, false)
    }

}