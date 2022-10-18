package com.micker.home.widget

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.core.Controller
import com.app.hubert.guide.listener.OnGuideChangedListener
import com.app.hubert.guide.model.GuidePage
import com.micker.core.rx.RxActivityResult
import com.micker.data.constant.LOGIN_ACTIVITY
import com.micker.data.model.home.StatusActionEntity
import com.micker.global.callback.PermissionCallback
import com.micker.global.user.AccountManager.isLogin
import com.micker.global.util.PermissionUtils
import com.micker.helper.Util
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.home.singleton.ColorsItemFactory
import com.micker.webview.Widget.WSCNWebView
import com.micker.webview.Widget.WebViewCompat
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.aqhy_view_svg_detail_header.view.*
import org.json.JSONObject

class SvgDetailHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var wscnWebView: WSCNWebView? = null

    init {
        setPadding(0, Util.getStatusBarHeight(getContext()), 0, 0)
        setBackgroundColor(Color.WHITE)
        LayoutInflater.from(getContext()).inflate(R.layout.aqhy_view_svg_detail_header, this, true)
        initListener()
        addNewGuide()
    }

    private fun addNewGuide() {
        NewbieGuide.with(context as Activity)
            .setLabel("top")
            .setShowCounts(1)
            .addGuidePage(
                GuidePage.newInstance()
                    .addHighLight(center_parent)
                    .setLayoutRes(R.layout.view_guide_simple)
                    .setOnLayoutInflatedListener { view, controller ->
                        val img = view.findViewById<ImageView>(R.id.img)
                        img.setImageResource(R.drawable.aqhy_new_guide1)
                        val params = img.layoutParams as LayoutParams
                        params.addRule(CENTER_HORIZONTAL)
                        params.topMargin =
                            Util.getStatusBarHeight(context) + ScreenUtils.dip2px(50f)
                        img.layoutParams = params
                    })
            .setOnGuideChangedListener(object : OnGuideChangedListener {
                override fun onRemoved(controller: Controller?) {
                    val footerView = (context as? Activity)?.findViewById<SvgDetailFooterView>(R.id.footer_view)
                    footerView?.addBottomGuide()
                }

                override fun onShowed(controller: Controller?) {
                    Log.i("guide", "show top")
                }

            })
            .show()
    }

    private fun initListener() {
        icon_back?.setOnClickListener {
            (context as? Activity)?.finish()
        }

        save?.setOnClickListener {
            PermissionUtils.requestPermission(context as Activity, object : PermissionCallback {
                override fun requestPermissSuccess() {
                    if (isLogin()) {
                        wscnWebView?.let {
                            showLoading()
                            WebViewCompat.loadJsFunction(it, "window.__YutaAppOnSaveData();")
                        }
                    } else {
                        RxActivityResult(it.context as? Activity)
                            .startForResult(
                                RouterHelper.getIntentFromUrl(
                                    LOGIN_ACTIVITY,
                                    it.context
                                )
                            )
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext {
                                wscnWebView?.let {
                                    showLoading()
                                    WebViewCompat.loadJsFunction(
                                        it,
                                        "window.__YutaAppOnSaveData();"
                                    )
                                }
                            }.subscribe()
                    }
                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        backward?.setOnClickListener {
            responseIsUndo(1)
        }

        forward?.setOnClickListener {
            responseIsUndo(0)
        }

        recover?.setOnClickListener {
            ColorsItemFactory.selectColorsItem(ColorsItemFactory.whiteColorsItem)
        }
    }

    private fun responseIsUndo(status: Int) {
        wscnWebView?.let {
            val jsonObject = JSONObject()
            jsonObject.put("undo", status)
            val value = jsonObject.toString()
            WebViewCompat.loadJsFunction(it, "window.__YutaAppOnStatus($value);")
        }
    }

    fun bindStatusData(entity: StatusActionEntity?) {
        entity?.apply {
            backward?.isEnabled = undo
            forward?.isEnabled = redo
        }
    }

    fun showLoading() {
        loading?.show()
        val animation1 = ObjectAnimator.ofFloat(save, "alpha", 1f, 0f)
        val animation2 = ObjectAnimator.ofFloat(loading, "alpha", 0f, 1f)
        val set = AnimatorSet()
        set.duration = 500
        set.interpolator = AccelerateInterpolator()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                save?.hide(false)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        set.playTogether(animation1, animation2)
        set.start()
    }

    fun hideLoading() {
        save?.show()
        val animation1 = ObjectAnimator.ofFloat(save, "alpha", 0f, 1f)
        val animation2 = ObjectAnimator.ofFloat(loading, "alpha", 1f, 0f)
        val set = AnimatorSet()
        set.duration = 500
        set.interpolator = AccelerateInterpolator()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                loading?.hide(false)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        set.playTogether(animation1, animation2)
        set.start()
    }
}