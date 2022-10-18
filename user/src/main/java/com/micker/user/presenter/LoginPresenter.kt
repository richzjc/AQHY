package com.micker.user.presenter

import android.text.TextUtils
import com.micker.core.base.BasePresenter
import com.micker.data.model.user.AccountUserInfoEntity
import com.micker.helper.snack.MToastHelper
import com.micker.rpc.ResponseListener
import com.micker.user.api.UserSignApi
import com.micker.user.callback.LoginCallback

class LoginPresenter : BasePresenter<LoginCallback>() {

    fun login(mobile : String?, password : String?){
        val value = checkData(mobile, password)
        if (!TextUtils.isEmpty(value)) {
            MToastHelper.showToast(value)
            return
        }
        UserSignApi(mobile!!, password!!, object : ResponseListener<AccountUserInfoEntity> {
            override fun onSuccess(model: AccountUserInfoEntity?, isCache: Boolean) {
                viewRef?.onLoginSuccess(model)
            }

            override fun onErrorResponse(code: Int, error: String?) {
                viewRef?.onLoginError()
            }
        }).start()
    }

    private fun checkData(username: String?, password: String?): String {
        if (TextUtils.isEmpty(username)) {
            return "请输入手机号"
        } else if (TextUtils.isEmpty(password)) {
            return "请输入密码"
        }
        return ""
    }
}