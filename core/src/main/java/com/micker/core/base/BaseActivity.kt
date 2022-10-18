package com.micker.core.base

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.micker.core.R
import com.micker.core.internal.ViewQuery
import com.micker.core.manager.AppManager
import com.micker.helper.system.WindowHelper
import com.umeng.analytics.MobclickAgent
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

abstract class BaseActivity<V, T : BasePresenter<V>> : SwipeBackActivity() {

    var mPresenter: T? = null
    var mViewQuery = ViewQuery()
    var dialogLoading: LoadingDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowHelper.toggleLightStatusBar(this, true)
        doInitCreate()
        setupWindowAnimations()
        setStatusBarTranslucentCompat()
    }

    private fun doInitCreate() {
        AppManager.getAppManager().addActivity(this)
        if (doGetContentViewId() > 0)
            setContentView(doGetContentViewId())

        swipeBackLayout?.setEnableGesture(isNeedSwipeBack())
        mViewQuery.setActivity(this)
        doInitPresenter()
        doInitSubViews(window.decorView)
        doInitData()
    }

    protected open fun doInitSubViews(view: View) {

    }

    protected open fun doInitData() {

    }

    abstract fun doGetContentViewId(): Int

    private fun doInitPresenter() {
        mPresenter = doGetPresenter()
        mPresenter?.attachViewRef(this as? V)
    }

    protected open fun doGetPresenter(): T? = null

    private fun setStatusBarTranslucentCompat() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            var visibility = window.decorView.systemUiVisibility
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = visibility
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    protected fun setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val slideTransition = Slide()
            slideTransition.slideEdge = Gravity.LEFT
            slideTransition.duration = 500
            window.reenterTransition = slideTransition
            window.exitTransition = slideTransition
        } else {
            overridePendingTransition(R.anim.push_left_in, 0)
        }
    }

    protected open fun isNeedSwipeBack() = true

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        AppManager.getAppManager().addActivity(this)
    }

    fun showDialog() {
        try {
            if (dialogLoading != null && dialogLoading!!.isAdded) {
                dialogLoading!!.dismiss()
            }
            dialogLoading = LoadingDialogFragment()
            if (!dialogLoading!!.isAdded) {
                dialogLoading?.show(supportFragmentManager, "loadingDialog")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun dismissDialog() {
        try {
            if (dialogLoading != null && dialogLoading!!.isAdded) {
                dialogLoading?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        MobclickAgent.onPageStart(localClassName)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        MobclickAgent.onPageEnd(localClassName)
        AppManager.getAppManager().removeActivity(this)
    }
}