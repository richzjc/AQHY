package com.micker.home.callback

import com.micker.core.callback.BaseRecyclerViewCallBack
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.model.inspiration.InspirationEntity


interface InspirationCallback : BaseRecyclerViewCallBack<InspirationEntity> {
    fun deleteSuccess(entity : InspirationEntity)
    fun requestSucc(model: List<LaunchConfigEntity>?)
}