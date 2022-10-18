package com.micker.home.presenter

import android.os.Bundle
import android.util.Log
import com.micker.core.base.BasePresenter
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.global.api.BaseListModel
import com.micker.global.api.BaseListResponse
import com.micker.global.api.realApi.SplashApi
import com.micker.helper.snack.MToastHelper
import com.micker.home.api.InspirationApi
import com.micker.home.api.SvgUserDeleteApi
import com.micker.home.api.SvgUserUpdateApi
import com.micker.home.callback.InspirationCallback
import com.micker.rpc.ResponseListener

class InspirationPresenter : BasePresenter<InspirationCallback>() {
    val listEntity = BaseListModel<InspirationEntity>()

    fun loadData(isRefresh : Boolean){
        if(isRefresh)
            listEntity.clearPage()

        val bundle = Bundle()
        bundle.putInt("page", listEntity.page)
        val inspirationAPi = InspirationApi(BaseListResponse(listEntity, viewRef), bundle)
        inspirationAPi.start()
    }

    fun delete(entity: InspirationEntity?) {
        entity?.let {
            SvgUserDeleteApi(object : ResponseListener<String> {
                override fun onSuccess(model: String?, isCache: Boolean) {
                    viewRef?.deleteSuccess(entity)
                }

                override fun onErrorResponse(code: Int, error: String?) {
                    MToastHelper.showToast("删除失败")
                }

            }, entity.id!!).start()
        }
    }

    fun updateContent(bundle : Bundle, status : Int) {
        SvgUserUpdateApi(object : ResponseListener<String> {
            override fun onSuccess(model: String?, isCache: Boolean) {
                loadData(true)
            }

            override fun onErrorResponse(code: Int, error: String?) {
                MToastHelper.showToast("操作失败")
            }

        }, bundle, status).start()
    }

    fun loadBanner() {
        val api = SplashApi("linggantop", "banner", object: ResponseListener<List<LaunchConfigEntity>> {
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