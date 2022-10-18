package com.micker.user.callback


interface ForgetPasswordCallback {

    fun onMsgSendSuccess()

    fun changePwdSuccess()

    fun registSuccess()

    fun loginChangeError()
}