package com.micker.aqhy.activity

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.micker.aqhy.R
import com.micker.aqhy.dialog.UserPrivacyDialog
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.global.const.imagesArry
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.file.FileUtil
import com.micker.helper.file.QDUtil.getShareImageCache
import com.micker.helper.router.DoubleClickHelper
import com.micker.helper.snack.MToastHelper
import kotlinx.android.synthetic.main.tail_activity_main_new.*
import kotlinx.android.synthetic.main.tail_activity_main_new.view.*
import kotlin.random.Random

class MainActivityNew : BaseActivity<Any, BasePresenter<Any>>() {
    override fun doGetContentViewId() = R.layout.tail_activity_main_new
    override fun isNeedSwipeBack() = false
    override fun doInitSubViews(view: View) {
        isShowPrivacy()
        initBg(view)
        initRv(view)

        stage?.bindData(11, "人", "入")
    }

    private fun initRv(view: View){
        view.recycleView?.setCanRefresh(false)
        view.recycleView?.isEnableLoadMore = false
        view.recycleView?.customRecycleView?.setLayoutManager(GridLayoutManager(this, 3))

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
}