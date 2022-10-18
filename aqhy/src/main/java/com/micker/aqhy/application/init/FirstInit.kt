package com.micker.aqhy.application.init

import android.app.Application
import android.content.Context
import com.kronos.router.Router
import com.micker.aqhy.application.BuglyInit
import com.micker.aqhy.application.ImageLoadOkHttpClient
import com.micker.aqhy.factory.ErrorCodeFactory
import com.micker.core.imageloader.ImageLoaderInit
import com.micker.data.constant.BASE_URL
import com.micker.global.user.AccountManager
import com.micker.helper.UtilsContextManager
import com.micker.rpc.VolleyQueue
import com.micker.rpc.host.HostManager
import com.micker.webview.Template.WSCNWebViewActivity

class FirstInit {
    fun init(application : Context){
        UtilsContextManager.getInstance().init(application as Application?).webViewActivity = WSCNWebViewActivity::class.java
        BuglyInit.init(application)
        VolleyQueue.getInstance().factory = ErrorCodeFactory()
        ImageLoaderInit.init(application, ImageLoadOkHttpClient().okHttpClient)
        HostManager.setBaseUrl(BASE_URL)
        Router.sharedRouter().attachApplication(application)
        initToken()
    }

    private fun initToken() {
        val map = mutableMapOf<String, String?>()
        if (AccountManager.isLogin()) {
            map["userId"] = AccountManager.getAccountUserId()
            map["token"] = AccountManager.getAccountToken()
            VolleyQueue.getInstance().addPostBody(map)
        } else {
            VolleyQueue.getInstance().postBody?.clear()
        }
    }
}