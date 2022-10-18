package com.richzjc.send_push.init

import android.app.Application
import android.content.Context
import com.kronos.router.Router
import com.micker.data.constant.BASE_URL
import com.micker.global.user.AccountManager
import com.micker.helper.UtilsContextManager
import com.micker.rpc.VolleyQueue
import com.micker.rpc.host.HostManager
import com.micker.webview.Template.WSCNWebViewActivity

class FirstInit {
    fun init(application : Context){
        UtilsContextManager.getInstance().init(application as Application?).webViewActivity = WSCNWebViewActivity::class.java
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