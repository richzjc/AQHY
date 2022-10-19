package com.micker.first

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import com.alibaba.fastjson.JSON
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.first.callback.DiyCallback
import com.micker.first.callback.NanduCallback
import com.micker.first.callback.SuccCallback
import com.micker.first.dialog.DiyDialog
import com.micker.first.dialog.NanduDialog
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

    private var guanKa: Int = 0

    private val nanDuCallback by lazy {
        object : NanduCallback {
            override fun nanduCallback(realjieshu: Int) {
                var jieshu = realjieshu
                SharedPrefsUtil.saveInt("first_stage_jieshu", jieshu)
                if (jieshu < 3)
                    jieshu = 3
                else if (jieshu > 11)
                    jieshu = 11

                stage.bindData(jieshu, stage.findWord, stage.proguardWord, succCallback)
                updateTvHint(stage.findWord)
            }
        }
    }

    private val diyCallback by lazy {
        object : DiyCallback {
            override fun diyCallback(findWord: String, proguardWord: String) {

                var jieshu = SharedPrefsUtil.getInt("first_stage_jieshu", 6)
                if (jieshu < 3)
                    jieshu = 3
                else if (jieshu > 11)
                    jieshu = 11

                stage.bindData(jieshu, findWord, proguardWord, succCallback)
                updateTvHint(findWord)
            }
        }
    }

    private val succCallback by lazy {
        object : SuccCallback {
            override fun succCallback() {
                if (list != null && list.size > 0) {
                    guanKa += 1
                    if (guanKa >= list.size)
                        guanKa = 0

                    val entity = list.get(guanKa)
                    SharedPrefsUtil.saveInt("first_stage_guanka", guanKa)
                    var jieshu = SharedPrefsUtil.getInt("first_stage_jieshu", 6)
                    if (jieshu < 3)
                        jieshu = 3
                    else if (jieshu > 11)
                        jieshu = 11

                    stage.bindData(jieshu, entity.findWord, entity.proguardWord, this)
                    updateTvHint(entity.findWord)
                }
            }
        }
    }

    private val list by lazy {
        val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets("first_stage.json"))
        val list = JSON.parseArray(json, FirstStageModel::class.java)
        list
    }

    private val drawable by lazy {
        val color = ResourceUtils.getColor(R.color.color_1482f0)
        ShapeDrawable.getDrawable(0, ScreenUtils.dip2px(5f), color, color)
    }

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

    private fun initBg() {
        val size = imagesArry.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadImage(imagesArry[index], bg, 0, false)
    }


    override fun doInitData() {
        super.doInitData()
        setListener()
        var jieshu = SharedPrefsUtil.getInt("first_stage_jieshu", 6)
        if (jieshu < 3)
            jieshu = 3
        else if (jieshu > 11)
            jieshu = 11

        guanKa = SharedPrefsUtil.getInt("first_stage_guanka", 0)

        if (list != null && list.size > 0) {
            stage.bindData(
                jieshu,
                list.get(guanKa).findWord,
                list.get(guanKa).proguardWord,
                succCallback
            )
            updateTvHint(list.get(guanKa).findWord)
        }
    }

    private fun updateTvHint(findWord : String){
        val build = SpannableStringBuilder()
        build.append("找出：")
        val startIndex = build.length
        build.append(findWord)
        val endIndex = build.length
        build.setSpan(AbsoluteSizeSpan(24, true), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        tv_hint?.text = build
    }

    private fun setListener() {
        last.setOnClickListener {
            if (list != null && list.size > 0) {
                guanKa -= 1
                if (guanKa < 0)
                    guanKa = list.size - 1

                val entity = list.get(guanKa)
                SharedPrefsUtil.saveInt("first_stage_guanka", guanKa)
                var jieshu = SharedPrefsUtil.getInt("first_stage_jieshu", 6)
                if (jieshu < 3)
                    jieshu = 3
                else if (jieshu > 11)
                    jieshu = 11

                stage.bindData(jieshu, entity.findWord, entity.proguardWord, succCallback)
                updateTvHint(entity.findWord)
            }
        }

        next.setOnClickListener {
            if (list != null && list.size > 0) {
                guanKa += 1
                if (guanKa >= list.size)
                    guanKa = 0

                val entity = list.get(guanKa)
                SharedPrefsUtil.saveInt("first_stage_guanka", guanKa)
                var jieshu = SharedPrefsUtil.getInt("first_stage_jieshu", 6)
                if (jieshu < 3)
                    jieshu = 3
                else if (jieshu > 11)
                    jieshu = 11

                stage.bindData(jieshu, entity.findWord, entity.proguardWord, succCallback)
                updateTvHint(entity.findWord)
            }
        }

        nandu.setOnClickListener {
            val dialog = NanduDialog()
            dialog?.nanduCallback = nanDuCallback
            dialog.show(supportFragmentManager, "nandu")
        }

        diy.setOnClickListener {
            val diyDialog = DiyDialog()
            diyDialog?.diyCallback = diyCallback
            diyDialog.show(supportFragmentManager, "diy")
        }
    }
}