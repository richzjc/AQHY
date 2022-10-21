package com.micker.third.activity

import android.os.Bundle
import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.first.callback.NanduCallback
import com.micker.first.callback.SuccCallback
import com.micker.first.dialog.NanduDialog
import com.micker.global.THIRD_STAGE_ROUTER
import com.micker.global.const.imagesArry
import com.micker.helper.SharedPrefsUtil
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_activity_third_stage.*
import kotlin.random.Random

@BindRouter(urls = [THIRD_STAGE_ROUTER])
class ThirdStageActivity : BaseActivity<Any, BasePresenter<Any>>() {


    private val nanDuCallback by lazy {
        object : NanduCallback {
            override fun nanduCallback(realjieshu: Int, isBiHua: Boolean) {
                if (!isBiHua) {
                    var jieshu = realjieshu
                    if (jieshu < 3)
                        jieshu = 3
                    else if (jieshu > 6)
                        jieshu = 6
                    SharedPrefsUtil.saveInt("third_stage_jieshu", jieshu)
                    not_edit_stage.bindData(jieshu, false, succCallback)
                    edit_stage.bindData(jieshu, true, succCallback)
                }
            }
        }
    }


    private val succCallback by lazy {
        object : SuccCallback {
            override fun succCallback() {
                setData(this)
            }
        }
    }

    private fun setData(succCallback: SuccCallback) {
        val jieshu = SharedPrefsUtil.getInt("third_stage_jieshu", 3)
        not_edit_stage.bindData(jieshu, false, succCallback)
        edit_stage.bindData(jieshu, true, succCallback)
    }

    override fun doGetContentViewId() = R.layout.aqhy_activity_third_stage

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        initBg()
    }

    private fun initBg() {
        val size = imagesArry.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadImage(imagesArry[index], bg, 0, false)
    }


    override fun doInitData() {
        super.doInitData()
        setListener()
        setData(succCallback)
    }


    private fun setListener() {
        nandu.setOnClickListener {
            val dialog = NanduDialog()
            val bundle = Bundle()
            bundle.putBoolean("isBiHua", false)
            bundle.putString("hint", "输入3~6的数字,越大越难")
            bundle.putString("title", "难度系数")
            dialog.arguments = bundle
            dialog?.nanduCallback = nanDuCallback
            dialog.show(supportFragmentManager, "nandu")
        }

        reset?.setOnClickListener {
            setData(succCallback)
        }
    }
}