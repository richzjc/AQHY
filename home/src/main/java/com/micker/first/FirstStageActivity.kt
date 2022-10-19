package com.micker.first

import android.view.View
import com.alibaba.fastjson.JSON
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.first.model.FirstStageModel
import com.micker.global.FIRST_STAGE_ROUTER
import com.micker.global.const.imagesArry
import com.micker.global.util.ShapeDrawable
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.file.CacheUtils
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_activity_first_stage.*
import kotlin.random.Random

@BindRouter(urls = [FIRST_STAGE_ROUTER])
class FirstStageActivity : BaseActivity<Any, BasePresenter<Any>>() {


    private val drawable by lazy {
        val color = ResourceUtils.getColor(R.color.color_1482f0)
        ShapeDrawable.getDrawable(0, ScreenUtils.dip2px(5f), color, color) }

    override fun doGetContentViewId() = R.layout.aqhy_activity_first_stage

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        initBg()
        ImageLoadManager.loadImage(R.drawable.drawable_first_stage_hint, iv_hint, 0)
        nandu.background = drawable
        diy.background = drawable
        last.background = drawable
        next.background = drawable
    }

    private fun initBg(){
        val size = imagesArry.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadImage(imagesArry[index], bg, 0, false)
    }


    override fun doInitData() {
        super.doInitData()
        val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets("first_stage.json"))
        val list = JSON.parseArray(json, FirstStageModel::class.java)
        var jieshu = SharedPrefsUtil.getInt("first_stage_jieshu", 6)
        if(jieshu < 3)
            jieshu = 3
        else if(jieshu > 11)
            jieshu = 11

        if(list != null && list.size > 0){
            stage.bindData(jieshu, list.get(0).findWord, list.get(0).proguardWord)
        }
    }
}