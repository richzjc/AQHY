package com.micker.aqhy.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.Log
import android.view.animation.DecelerateInterpolator
import com.micker.aqhy.R
import com.micker.aqhy.application.BuglyInit
import com.micker.aqhy.application.init.UmengInit
import com.micker.aqhy.callback.SplashCallback
import com.micker.aqhy.dialog.UserPrivacyDialog
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.aqhy.presenter.SplashPresenter
import com.micker.core.base.BaseActivity
import com.micker.core.imageloader.ImageLoadManager
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.system.ScreenUtils
import kotlinx.android.synthetic.main.tail_activity_splash.*
import kotlin.random.Random

class SplashActivity : BaseActivity<SplashCallback, SplashPresenter>(), SplashCallback {

    private var isSend = false
    private var dialog: UserPrivacyDialog? = null


    private val handler = Handler(Handler.Callback {

        if (SharedPrefsUtil.getBoolean("needShowGuide", true)) {
            SharedPrefsUtil.saveBoolean("needShowGuide", false)
            val intent = Intent(this@SplashActivity, GuideActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@SplashActivity, MainActivityNew::class.java)
            startActivity(intent)
            finish()
        }
        return@Callback true
    })

    override fun doGetContentViewId(): Int = R.layout.tail_activity_splash

    override fun doInitData() {
        super.doInitData()
        img.setActualImageResource(0)
//        mPresenter?.requestImgs()

        if (SharedPrefsUtil.getBoolean("userPrivacy", true)) {
            dialog = UserPrivacyDialog()
            dialog?.setOnDismissListener {
                sendMsg()
            }
            dialog?.show(supportFragmentManager, "userPrivacy")
        }else{
            BuglyInit.init(this)
            UmengInit().init(this)
        }


        startAnimation()
    }

    private fun startAnimation() {
        val animator1 = ObjectAnimator.ofFloat(
            parent_view,
            "translationY",
            ScreenUtils.dip2px(40f).toFloat(),
            0f
        )
        val animator2 = ObjectAnimator.ofFloat(parent_view, "alpha", 0f, 1f)
        val set = AnimatorSet()
        set.duration = 800
        set.interpolator = DecelerateInterpolator()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!(dialog != null && dialog!!.isAdded))
                    sendMsg()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        set.playTogether(animator1, animator2)
        set.start()
    }

    override fun doGetPresenter() = SplashPresenter()

    override fun requestSucc(model: List<LaunchConfigEntity>?) {
        model?.also {
            val index = (it.indices).random()
            Log.i("index", "$index")
            val url = it[index].image
            ImageLoadManager.loadBitmap(url) { inner ->
                if (inner == null)
                    return@loadBitmap

                var widthScale = 0.0f
                var heightScale = 0.0f
                if (inner.width > ScreenUtils.getScreenWidth())
                    widthScale = (ScreenUtils.getScreenWidth() * 1.0f) / inner.width
                if (inner.height > ScreenUtils.getScreenHeight() - ScreenUtils.dip2px(200f))
                    heightScale =
                        (ScreenUtils.getScreenHeight() - ScreenUtils.dip2px(200f)) * 1.0f / inner.height

                val realScale = Math.max(widthScale, heightScale)
                if (realScale > 0) {
                    val maxtrix = Matrix()
                    maxtrix.postScale(realScale, realScale)
                    val bihuanbmp =
                        Bitmap.createBitmap(inner, 0, 0, inner.width, inner.height, maxtrix, true);
                    img.setImageBitmap(bihuanbmp)
                } else {
                    img.setImageBitmap(inner)
                }
                startImgAnim()
            }
        }
        sendMsg()
    }


    private fun startImgAnim() {
        val animator1 = ObjectAnimator.ofFloat(
            img,
            "scaleX",
            0.5f,
            1f
        )

        val animator2 = ObjectAnimator.ofFloat(img, "alpha", 0f, 1f)

        val animator3 = ObjectAnimator.ofFloat(
            img,
            "scaleY",
            0.5f,
            1f
        )

        val set = AnimatorSet()
        set.duration = 300
        set.interpolator = DecelerateInterpolator()
        set.playTogether(animator1, animator2, animator3)
        set.start()
    }

    private fun sendMsg() {
        if (!isSend) {
            handler.sendEmptyMessageDelayed(1, 1000)
        }
        isSend = true
    }

    override fun reqeustFailed() {
        sendMsg()
    }

    override fun isNeedSwipeBack() = false

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(1)
    }
}