package com.micker.six.activity

import android.view.View
import com.alibaba.fastjson.JSON
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.global.SIX_STAGE_ROUTER
import com.micker.global.const.imagesArry
import com.micker.helper.file.CacheUtils
import com.micker.home.R
import com.micker.six.model.SixListModel
import com.micker.six.model.SixModel
import kotlinx.android.synthetic.main.aqhy_activity_six_stage.*
import kotlinx.android.synthetic.main.aqhy_activity_six_stage.bg
import org.json.JSONObject
import kotlin.random.Random

@BindRouter(urls = [SIX_STAGE_ROUTER])
class SixActivity : BaseActivity<Any, BasePresenter<Any>>() {

    override fun doGetContentViewId() = R.layout.aqhy_activity_six_stage
    override fun isNeedSwipeBack() =  false
    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        initBg()
        val jsonkey = intent?.getStringExtra("json")
        val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets("${jsonkey}.json"))
        val jobj = JSONObject(json)
        val list = ArrayList<SixListModel>()
        jobj.keys().forEach {
            val model = SixListModel()
            model.key = it
            model.list = JSON.parseArray(jobj.optString(it), SixModel::class.java)
            list.add(model)
        }

        stage?.bindData(list.get(0))
    }


    private fun initBg() {
        val size = imagesArry.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadImage(imagesArry[index], bg, 0, false)
    }

    override fun doInitData() {
        super.doInitData()
        setListener()
    }


    private fun setListener(){
        next?.setOnClickListener {
            stage?.next()
        }

        reset?.setOnClickListener {
            stage?.reset()
        }
    }
}