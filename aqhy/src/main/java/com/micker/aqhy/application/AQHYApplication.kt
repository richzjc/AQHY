package com.micker.aqhy.application

import android.app.Application
import com.facebook.stetho.Stetho
import com.micker.data.constant.ACCOUNT_STATE_CHANGED
import com.micker.global.user.AccountManager
import com.micker.helper.ResourceUtils
import com.micker.helper.observer.Observer
import com.micker.helper.observer.ObserverManger
import com.micker.rpc.VolleyQueue
import com.umeng.commonsdk.UMConfigure

class AQHYApplication : Application(), Observer {

    override fun onCreate() {
        super.onCreate()
        val umengKey = ResourceUtils.getMetaDateFromName("UMENG_APPKEY")
        val umengMessageScret = ResourceUtils.getMetaDateFromName("UMENG_MESSAGE_SCRET")
        UMConfigure.preInit(this, umengKey, umengMessageScret)
        Stetho.initializeWithDefaults(this)
        ObserverManger.getInstance().registerObserver(this, ACCOUNT_STATE_CHANGED)
    }

    override fun update(id: Int, vararg args: Any?) {
        when (id) {
            ACCOUNT_STATE_CHANGED -> initToken()
        }
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