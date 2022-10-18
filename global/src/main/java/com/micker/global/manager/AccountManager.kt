package com.micker.global.user

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.micker.data.constant.ACCOUNT_STATE_CHANGED
import com.micker.data.constant.LOGIN_ACTIVITY
import com.micker.data.model.user.AccountUserInfoEntity
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.observer.ObserverManger
import com.micker.helper.router.DoubleClickHelper
import com.micker.helper.router.RouterHelper

object AccountManager {

    val MOBILE = "mobile"
    val DATA = "data"


    var userInfoEntity: AccountUserInfoEntity? = null

    init {
        userInfoEntity = getCurrentUserInfoEntity()
    }

    fun getAccountUserId(): String {
        return userInfoEntity?.userId ?: ""
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(userInfoEntity?.token)
    }

    fun isLogined(context: Context?, needToShowLogin: Boolean, bundle: Bundle?): Boolean {
        if (!isLogin()) {
            if (null != context && needToShowLogin) {
                DoubleClickHelper.cleanDownTime()
                RouterHelper.open(LOGIN_ACTIVITY, context, bundle)
            }
            return false
        } else {
            return true
        }
    }

    fun getAccountToken(): String? {
        return userInfoEntity?.token
    }

    fun getLoginMobile() = SharedPrefsUtil.getString(MOBILE, "")!!

    fun saveUserInfo(entity: AccountUserInfoEntity?) {
        this.userInfoEntity = entity
        if (entity != null) {
            val json = JSON.toJSONString(entity)
            Log.i("json", json)
            SharedPrefsUtil.saveString(DATA, json)
            SharedPrefsUtil.saveString(MOBILE, entity?.baseUser?.phone)
        } else {
            clear()
        }
    }

    private fun getCurrentUserInfoEntity(): AccountUserInfoEntity? {
        val json = SharedPrefsUtil.getString(DATA, "")
        return if (!TextUtils.isEmpty(json)) {
            JSON.parseObject(json, AccountUserInfoEntity::class.java)
        } else {
            null
        }
    }


    fun clear() {
        userInfoEntity = null
        SharedPrefsUtil.remove(DATA)
    }

    fun logout() {
        clear()
        ObserverManger.getInstance().notifyObserver(ACCOUNT_STATE_CHANGED)
    }
}