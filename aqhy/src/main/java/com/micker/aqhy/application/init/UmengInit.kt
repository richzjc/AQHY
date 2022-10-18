package com.micker.aqhy.application.init

import android.content.Context
import com.mcxiaoke.packer.helper.PackerNg
import com.micker.aqhy.application.UmengConfig
import com.micker.helper.ResourceUtils
import com.micker.push.UmengPushAdapter
import com.umeng.commonsdk.UMConfigure

class UmengInit {
    fun init(application: Context?) {
        umengInit(application)
        pushInit(application)
    }

    private fun umengInit(application: Context?) {
        val umengKey = ResourceUtils.getMetaDateFromName("UMENG_APPKEY")
        val umengMessageScret = ResourceUtils.getMetaDateFromName("UMENG_MESSAGE_SCRET")
        val channel = PackerNg.getChannel(application)
        UMConfigure.init(
            application,
            umengKey,
            channel,
            UMConfigure.DEVICE_TYPE_PHONE,
            umengMessageScret
        )
        UmengConfig.setId()
    }

    private fun pushInit(application: Context?) {
        val push = UmengPushAdapter()
        push?.init(application)
    }
}