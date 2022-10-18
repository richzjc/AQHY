package com.micker.home.presenter

import android.os.Bundle
import android.util.Log
import com.micker.core.base.BasePresenter
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.model.home.HomeEntity
import com.micker.global.api.BaseListModel
import com.micker.global.api.BaseListResponse
import com.micker.global.api.realApi.SplashApi
import com.micker.home.api.HomeApi
import com.micker.home.callback.HomeCallback
import com.micker.rpc.ResponseListener

class HomePresenter : BasePresenter<HomeCallback>() {
    val listEntity = BaseListModel<HomeEntity>()

    fun loadData(isRefresh : Boolean) {
        if(isRefresh)
            listEntity.clearPage()

        val bundle = Bundle()
        bundle.putInt("page", listEntity.page)
        val homeApi = HomeApi(BaseListResponse(listEntity, viewRef), bundle)
        homeApi.start()
    }

    fun loadBaner() {
        val api = SplashApi("hometop", "banner",  object: ResponseListener<List<LaunchConfigEntity>> {
            override fun onSuccess(model: List<LaunchConfigEntity>?, isCache: Boolean) {
                viewRef?.requestSucc(model)
            }

            override fun onErrorResponse(code: Int, error: String?) {
                Log.i("splash", "failed")
            }
        })
        api.setNeedToast(false)
        api.start()
    }
}