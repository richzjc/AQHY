package com.micker.home.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.micker.core.activity.GestureSetup
import com.micker.core.base.BaseActivity
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.helper.observer.Observer
import com.micker.helper.observer.ObserverIds
import com.micker.helper.observer.ObserverManger
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.home.callback.SvgDetailWebViewCallback
import com.micker.home.presenter.SvgDetailPresenter
import com.micker.home.singleton.ColorsItemFactory
import com.micker.home.utils.isSinglePixel
import com.micker.webview.Widget.WSCNWebClient
import com.micker.webview.Widget.WebViewCompat
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.aqhy_view_svg_detail_content.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory

class SvgDetailContentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), Observer {
    var mPresenter: SvgDetailPresenter? = null
    var disposable: Disposable? = null

    init {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_f8f8f8))
        LayoutInflater.from(getContext()).inflate(R.layout.aqhy_view_svg_detail_content, this, true)
        ObserverManger.getInstance().registerObserver(this, ObserverIds.COLORS_ITEM_SELECTED)
        initListener()
        initView()
    }

    private fun initListener() {
        error_hint?.setOnClickListener {
            (it.context as? BaseActivity<*, *>)?.showDialog()
            WebViewCompat.loadJsFunction(wscnWebView, "window.__YutaAppOnPrepare()")
        }

        wscnWebView.webChromeClient = WebChromeClient()

        wscnWebView.webViewClient = object : WSCNWebClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                RouterHelper.open(request.url.toString(), view.context)
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                RouterHelper.open(url, view.context)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Log.i("page", "finish")
                wscnWebView?.show()
            }
        }
    }

    private fun initView() {
        val gesture = GestureSetup()
        gesture.onSetupGestureView(layout)
        if (mPresenter != null) {
            wscnWebView.addMethod(SvgDetailWebViewCallback(mPresenter))
            wscnWebView.loadHtml("newsnode", "index.html")
        } else {
            Observable.just(0)
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    wscnWebView.addMethod(SvgDetailWebViewCallback(mPresenter))
                    wscnWebView.loadHtml("newsnode", "index.html")
                }
                .subscribe()
        }
    }

    fun loadSvgSucc(svgPath: String?, callbackId: String?, isReset: Boolean) {
        error_hint?.hide(true)
        layout?.show()
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(ByteArrayInputStream(svgPath?.toByteArray(Charset.forName("UTF-8"))))
        val rootElement = doc.documentElement
        val width = rootElement.getAttribute("width").toFloat()
        val height = rootElement.getAttribute("height").toFloat()
        val params = wscnWebView.layoutParams as FrameLayout.LayoutParams
        params.width = ScreenUtils.getScreenWidth()
        params.height = ((ScreenUtils.getScreenWidth() * height) / width).toInt()
        wscnWebView?.layoutParams = params
        layout?.show()
        try {
            if (disposable != null && !disposable!!.isDisposed)
                disposable?.dispose()
            disposable = isSinglePixel(wscnWebView)
            wscnWebView.loadJavaScript(callbackId, getBridgeJson(true, svgPath, isReset))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getBridgeJson(success: Boolean, data: String?, isReset: Boolean): String {
        val jsonObject = JSONObject()
        try {
            if (success) {
                jsonObject.put("message", "success")
            } else {
                jsonObject.put("message", "fail")
            }
            val resultObject = JSONObject()
            val jobj = JSONObject()
            if (isReset)
                jobj.put("reset", "reset")
            else
                jobj.put("reset", "no")
            jobj.put("content", data)
            resultObject.put("data", jobj)
            jsonObject.put("results", resultObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject.toString()
    }

    fun loadSvgError() {
        error_hint?.show()
        layout?.hide(true)
    }

    override fun update(id: Int, vararg args: Any?) {
        if (id == ObserverIds.COLORS_ITEM_SELECTED) {
            ColorsItemFactory.loadFill(wscnWebView)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        wscnWebView?.destroy()
        ObserverManger.getInstance().removeObserver(this)
        if (disposable != null && !disposable!!.isDisposed)
            disposable?.dispose()
    }

    fun bindHomeSubItemEntity(entity: HomeSubItemEntity?) {
        wscnWebView?.setTag(R.id.tag_sub_item_entity, entity)
    }
}