package com.micker.five.api

import android.os.Bundle
import com.micker.rpc.CustomApi

class FiveApi(bundle: Bundle?) : CustomApi<String>(bundle) {
    override fun getUrl() = bundle?.getString("apiPath", "")
}