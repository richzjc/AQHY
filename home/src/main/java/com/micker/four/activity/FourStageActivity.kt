package com.micker.four.activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.first.callback.NanduCallback
import com.micker.first.callback.SuccCallback
import com.micker.first.dialog.NanduDialog
import com.micker.global.FOUR_STAGE_ROUTER
import com.micker.global.THIRD_STAGE_ROUTER
import com.micker.global.const.imagesArry
import com.micker.global.const.pingTuArr
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.*
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.bg
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.edit_stage
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.nandu
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.reset
import kotlin.random.Random

@BindRouter(urls = [FOUR_STAGE_ROUTER])
class FourStageActivity : BaseActivity<Any, BasePresenter<Any>>() {


    private val nanDuCallback by lazy {
        object : NanduCallback {
            override fun nanduCallback(realjieshu: Int, isBiHua: Boolean) {
                if (!isBiHua) {
                    var jieshu = realjieshu
                    if (jieshu < 3)
                        jieshu = 3
                    else if (jieshu > 6)
                        jieshu = 6
                    SharedPrefsUtil.saveIntForInstance("four_stage_jieshu", jieshu)
                    reset?.performClick()
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
        val jieshu = SharedPrefsUtil.getInt("four_stage_jieshu", 3)
        val size = pingTuArr.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadBitmap(pingTuArr[index]) {
            if (it != null) {
                edit_stage.bindData(jieshu, it, succCallback)
                setNotEditBitmap(it)
            } else {
                val drawable = ResourceUtils.getResDrawableFromID(R.drawable.defalut_pingtu_img)
                val bitmapDrawable = drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                edit_stage?.bindData(jieshu, bitmap, succCallback)
                setNotEditBitmap(bitmap)
            }
        }
    }

    private fun setNotEditBitmap(bitmap: Bitmap) {
        not_edit_stage?.also {
            val height = ScreenUtils.getScreenHeight() / 3 - ScreenUtils.dip2px(50f)
            var width = (bitmap.width / bitmap.height) * height
            if (width > ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(30f))
                width = ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(30f)
            val params = FrameLayout.LayoutParams(width, height)
            it.layoutParams = params
            it.scaleType = ImageView.ScaleType.FIT_XY
            it.setImageBitmap(bitmap)
        }
    }

    override fun doGetContentViewId() = R.layout.aqhy_activity_four_stage

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
            dialog.show(supportFragmentManager, "thirdNanDu")
        }

        reset?.setOnClickListener {
            val jieshu = SharedPrefsUtil.getInt("four_stage_jieshu", 3)
            edit_stage?.bindData(jieshu, edit_stage.originBitmap, succCallback)
        }
    }
}