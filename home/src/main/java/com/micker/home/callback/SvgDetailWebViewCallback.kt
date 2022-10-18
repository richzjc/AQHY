package com.micker.home.callback

import com.micker.home.presenter.SvgDetailPresenter
import com.micker.home.singleton.ColorsItemFactory
import com.micker.webview.Widget.WSCNWebView
import com.micker.webview.javascript.CustomCallBack
import org.json.JSONObject


class SvgDetailWebViewCallback(presenter: SvgDetailPresenter?) : CustomCallBack() {
    private var mPresenter: SvgDetailPresenter? = null

    init {
        this.callbackFunctionName = "Load,Status"
        this.mPresenter = presenter
    }


    override fun onCallBack(view: WSCNWebView?, function: String?, args: JSONObject?) {
        if (function == "loadData") {
            val callbackId = args?.optString("callbackId")
            mPresenter?.loadColorType()
            mPresenter?.loadPath(callbackId)
        } else if (function == "loadFill") {
           ColorsItemFactory.loadFill(view)
        } else if (function == "saveData") {
            mPresenter?.saveData(args, view)
        } else if (function == "action") {
            mPresenter?.statusAction(args)
        }
    }
}