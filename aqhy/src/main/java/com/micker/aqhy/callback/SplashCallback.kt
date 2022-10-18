package com.micker.aqhy.callback

import com.micker.data.model.aqhy.LaunchConfigEntity

interface SplashCallback {
    fun requestSucc(model: List<LaunchConfigEntity>?)
    fun reqeustFailed()
}