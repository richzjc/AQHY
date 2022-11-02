package com.micker.five.api

import android.os.Bundle
import com.micker.rpc.CustomApi
import com.micker.rpc.ResponseListener

class FiveApi(bundle: Bundle?, listener : ResponseListener<String>?) : CustomApi<String>(listener, bundle) {
    init {
        isNeedRefresh = true
    }
    override fun getUrl() = bundle?.getString("apiPath", "")
}