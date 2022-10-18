package com.micker.user.presenter

import com.micker.core.base.BasePresenter
import com.micker.helper.snack.MToastHelper
import com.micker.rpc.ResponseListener
import com.micker.user.api.ChangePwdApi
import com.micker.user.api.UserRegistApi
import com.micker.user.api.VerifyCodeApi
import com.micker.user.callback.ForgetPasswordCallback

class ForgetPasswordPresenter : BasePresenter<ForgetPasswordCallback>() {

    fun getVerifyCode(mobile: String) {
        VerifyCodeApi(mobile, object : ResponseListener<String> {
            override fun onSuccess(model: String?, isCache: Boolean) {
                viewRef?.onMsgSendSuccess()
            }

            override fun onErrorResponse(code: Int, error: String?) {
                MToastHelper.showToast("发送验证码失败")
            }
        }).start()
    }

    fun changePwd(phone: String?, code: String?, pwd: String) {
        ChangePwdApi(phone, code, pwd, object : ResponseListener<String> {
            override fun onSuccess(model: String?, isCache: Boolean) {
                viewRef?.changePwdSuccess()
            }

            override fun onErrorResponse(code: Int, error: String?) {
                MToastHelper.showToast("修改失败")
                viewRef?.loginChangeError()
            }
        }).start()
    }

    fun regist(mobile : String, code : String, pwd : String?, name : String?) {
        UserRegistApi(mobile, code, pwd, name, object : ResponseListener<String>{
            override fun onSuccess(model: String?, isCache: Boolean) {
                viewRef?.registSuccess()
            }

            override fun onErrorResponse(code: Int, error: String?) {
                MToastHelper.showToast("注册失败")
                viewRef?.loginChangeError()
            }

        }).start()
    }
}