package com.micker.home.presenter

import android.os.Bundle
import com.micker.core.base.BasePresenter
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.global.api.BaseListModel
import com.micker.global.api.BaseListResponse
import com.micker.home.api.HomeCategoryListApi
import com.micker.home.callback.HomeCategoryListCallback

class HomeCategoryListPresenter : BasePresenter<HomeCategoryListCallback>() {
    val listEntity = BaseListModel<HomeSubItemEntity>()

    fun loadData(groupId : String?, isRefresh : Boolean){
        if(isRefresh)
            listEntity.clearPage()

        val bundle = Bundle()
        bundle.putInt("page", listEntity.page)
        bundle.putString("groupId", groupId)
        val homeCategoryApi = HomeCategoryListApi(BaseListResponse(listEntity, viewRef), bundle)
        homeCategoryApi.start()
    }
}