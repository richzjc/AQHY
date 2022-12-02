package com.micker.first

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.first.callback.DiyCallback
import com.micker.first.callback.NanduCallback
import com.micker.first.callback.SuccCallback
import com.micker.first.dialog.DiyDialog
import com.micker.first.dialog.NanduDialog
import com.micker.global.FIRST_STAGE_ROUTER
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.file.CacheUtils
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_activity_first_stage.*
import kotlin.random.Random

@BindRouter(urls = [FIRST_STAGE_ROUTER])
class FirstStageActivity : BaseActivity<Any, BasePresenter<Any>>() {


    private val nanDuCallback by lazy {
        object : NanduCallback {
            override fun nanduCallback(realjieshu: Int, isBiHua: Boolean) {
                if (!isBiHua) {
                    var jieshu = realjieshu
                    if (jieshu < 3)
                        jieshu = 3
                    else if (jieshu > 11)
                        jieshu = 11
                    SharedPrefsUtil.saveInt("first_stage_jieshu", jieshu)
                    stage.bindData(jieshu, stage.findWord, stage.proguardWord, succCallback)
                    updateTvHint(stage.findWord)
                } else {
                    var jieshu = realjieshu

                    if (jieshu < 1)
                        jieshu = 1
                    else if (jieshu > 17)
                        jieshu = 11

                    SharedPrefsUtil.saveInt("first_stage_bihua", jieshu)
                    setData(jieshu, succCallback)
                }
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
                setData(-1, this)
            }
        }
    }

    private fun setData(bihua: Int, succCallback: SuccCallback) {
        var realBiBua = bihua
        if (realBiBua < 1 || realBiBua > 17) {
            realBiBua = SharedPrefsUtil.getInt("first_stage_bihua", 0)
        }
        var jieshu = SharedPrefsUtil.getInt("first_stage_jieshu", 6)
        if (jieshu < 3)
            jieshu = 3
        else if (jieshu > 11)
            jieshu = 11

        if (realBiBua < 1 || realBiBua > 17) {
            val radomFind = Random.nextInt(17)
            val findStr = list.optString("${radomFind + 1}", "").replace("\n", "")
            val findSize = findStr.length
            val findIndex = Random.nextInt(findSize)
            val findString = findStr.toCharArray().get(findIndex).toString()

            stage?.bindData(jieshu, findString, findStr, succCallback)
            updateTvHint(findString)
        } else {
            val findStr = list.optString("${realBiBua}", "").replace("\n", "")
            val findSize = findStr.length
            val findIndex = Random.nextInt(findSize)
            val findString = findStr.toCharArray().get(findIndex).toString()


            stage?.bindData(jieshu, findString, findStr, succCallback)
            updateTvHint(findString)
        }
    }


    private val list by lazy {
        val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets("first_stage.json"))
        val jobj = org.json.JSONObject(json)
        jobj
    }


    override fun doGetContentViewId() = R.layout.aqhy_activity_first_stage

    override fun doInitData() {
        super.doInitData()
        setListener()
        setData(-1, succCallback)
    }

    private fun updateTvHint(findWord: String) {
        val build = SpannableStringBuilder()
        build.append("找出：")
        val startIndex = build.length
        build.append(findWord)
        val endIndex = build.length
        build.setSpan(
            AbsoluteSizeSpan(24, true),
            startIndex,
            endIndex,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        tv_hint?.text = build
    }

    private fun setListener() {
        nandu.setOnClickListener {
            val dialog = NanduDialog()
            val bundle = Bundle()
            bundle.putBoolean("isBiHua", false)
            bundle.putString("hint", "输入3~11的数字,越大越难")
            bundle.putString("title", "难度系数")
            dialog.arguments = bundle
            dialog?.nanduCallback = nanDuCallback
            dialog.show(supportFragmentManager, "nandu")
        }

        bihua.setOnClickListener {
            val dialog = NanduDialog()
            val bundle = Bundle()
            bundle.putBoolean("isBiHua", true)
            bundle.putString("hint", "请输入笔画数（1~17）画的汉字")
            bundle.putString("title", "笔画数")
            dialog.arguments = bundle
            dialog?.nanduCallback = nanDuCallback
            dialog.show(supportFragmentManager, "笔画")
        }

        diy.setOnClickListener {
            val diyDialog = DiyDialog()
            diyDialog?.diyCallback = diyCallback
            diyDialog.show(supportFragmentManager, "diy")
        }
    }
}