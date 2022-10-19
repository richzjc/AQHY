package com.micker.aqhy.activity

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.micker.aqhy.R
import com.micker.aqhy.adapter.MainGridAdapter
import com.micker.aqhy.dialog.UserPrivacyDialog
import com.micker.aqhy.model.MainGridModel
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.global.const.imagesArry
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.file.CacheUtils
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

    }

    private fun initRv(view: View){
        view.recycleView?.setCanRefresh(false)
        view.recycleView?.isEnableLoadMore = false
        view.recycleView?.customRecycleView?.setLayoutManager(GridLayoutManager(this, 3))
//        view.recycleView?.customRecycleView?.addItemDecoration(GridItemDecoration(3))
        val adapter = MainGridAdapter()
        val json = CacheUtils.InputStreamToString(CacheUtils.getFileFromAssets("func.json"))
        val list = JSON.parseArray(json, MainGridModel::class.java)
        adapter.setData(list)
        view.recycleView?.customRecycleView?.adapter = adapter
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

    private class GridItemDecoration(val spanCount : Int) : RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

        }
    }
}