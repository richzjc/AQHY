package com.micker.aqhy.activity

import android.view.View
import com.alibaba.fastjson.JSON
import com.micker.aqhy.R
import com.micker.aqhy.dialog.UserPrivacyDialog
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.global.FIRST_STAGE_ROUTER
import com.micker.global.const.imagesArry
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.file.CacheUtils
import com.micker.helper.file.FileUtil
import com.micker.helper.file.QDUtil.getShareImageCache
import com.micker.helper.router.DoubleClickHelper
import com.micker.helper.router.RouterHelper
import com.micker.helper.snack.MToastHelper
import kotlinx.android.synthetic.main.tail_activity_main_new.*
import kotlinx.android.synthetic.main.tail_activity_main_new.view.*
import kotlin.random.Random

class MainActivityNew : BaseActivity<Any, BasePresenter<Any>>() {

    /**
     * http://www.qt86.com/
     * logo生成规则：
     * 汉仪蝶语体简
     * https://www.uupoop.com/#/editor/ 图片制作网站
     * 图片尺寸176*176
     */

    override fun doGetContentViewId() = R.layout.tail_activity_main_new
    override fun isNeedSwipeBack() = false
    override fun doInitSubViews(view: View) {
        isShowPrivacy()
        initBg(view)
        initListener()
    }

    private fun initBg(view: View){
        val size = imagesArry.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadImage(imagesArry[index], view.bg, 0, false)
    }

    private fun isShowPrivacy(){
        if(SharedPrefsUtil.getBoolean("userPrivacy", true)){
            val dialog = UserPrivacyDialog()
            dialog.show(supportFragmentManager, "userPrivacy")
        }
    }

    override fun onBackPressed() {
        when {
            !DoubleClickHelper.checkExitDoubleClick() -> {
                super.onBackPressed()
            }
            else -> MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.tail_exit_app_text))
        }
    }

    override fun onDestroy() {
        FileUtil.deleteDirectory(getShareImageCache(this))
        super.onDestroy()
    }

    private fun initListener(){
        first_stage?.setOnClickListener { RouterHelper.open(FIRST_STAGE_ROUTER, this) }
    }
}