package com.micker.aqhy.application

import android.app.Application
import com.facebook.stetho.Stetho
import com.micker.data.constant.ACCOUNT_STATE_CHANGED
import com.micker.global.user.AccountManager
import com.micker.helper.observer.Observer
import com.micker.helper.observer.ObserverManger
import com.micker.rpc.VolleyQueue

class AQHYApplication : Application(), Observer {

    override fun onCreate() {
        super.onCreate()
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