package com.micker.user.callback

import com.micker.data.model.user.AccountUserInfoEntity


interface LoginCallback {
    fun onLoginSuccess(entity : AccountUserInfoEntity?)
    fun onLoginError()
}