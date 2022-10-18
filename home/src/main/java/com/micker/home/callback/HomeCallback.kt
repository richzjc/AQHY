package com.micker.home.callback

import com.micker.core.callback.BaseRecyclerViewCallBack
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.model.home.HomeEntity

interface HomeCallback : BaseRecyclerViewCallBack<HomeEntity> {
    fun requestSucc(model: List<LaunchConfigEntity>?)
}