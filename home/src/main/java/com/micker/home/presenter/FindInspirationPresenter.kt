package com.micker.home.presenter

import android.os.Bundle
import com.micker.core.base.BasePresenter
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.global.api.BaseListModel
import com.micker.global.api.BaseListResponse
import com.micker.helper.snack.MToastHelper
import com.micker.home.api.SvgSameAllApi
import com.micker.home.api.SvgUserDeleteApi
import com.micker.home.api.SvgUserUpdateApi
import com.micker.home.callback.InspirationCallback
import com.micker.rpc.ResponseListener

class FindInspirationPresenter : BasePresenter<InspirationCallback>() {
    val listEntity = BaseListModel<InspirationEntity>()

    fun loadData(isRefresh: Boolean, svgId : String?) {
        if (isRefresh)
            listEntity.clearPage()

        val bundle = Bundle()
        bundle.putInt("page", listEntity.page)
        bundle.putString("svgId", svgId)
        val inspirationAPi = SvgSameAllApi(BaseListResponse(listEntity, viewRef), bundle)
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

            }, entity!!.id!!).start()
        }
    }

    fun updateContent(bundle : Bundle, status : Int, svgId: String?) {
        SvgUserUpdateApi(object : ResponseListener<String> {
            override fun onSuccess(model: String?, isCache: Boolean) {
                loadData(true, svgId)
            }

            override fun onErrorResponse(code: Int, error: String?) {
                MToastHelper.showToast("操作失败")
            }

        }, bundle, status).start()
    }
}