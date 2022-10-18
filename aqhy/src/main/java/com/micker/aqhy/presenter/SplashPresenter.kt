package com.micker.aqhy.presenter

import android.util.Log
import com.micker.global.api.realApi.SplashApi
import com.micker.aqhy.callback.SplashCallback
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.core.base.BasePresenter
import com.micker.rpc.ResponseListener

class SplashPresenter : BasePresenter<SplashCallback>() {

    fun requestImgs(){
        val api = SplashApi("launch", "result", object :
            ResponseListener<List<LaunchConfigEntity>> {
            override fun onSuccess(model: List<LaunchConfigEntity>?, isCache: Boolean) {
                viewRef?.requestSucc(model)
            }

            override fun onErrorResponse(code: Int, error: String?) {
                Log.i("splash", "failed")
                viewRef?.reqeustFailed()
            }
        })
        api.setNeedToast(false)
        api.start()
    }
}