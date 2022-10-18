package com.micker.home.api

import com.micker.rpc.CustomApi
import com.micker.rpc.ResponseListener


class SvgPathApi(listener : ResponseListener<String>, var apiUrl  : String?) : CustomApi<String>(listener) {
    init {
        setCacheTime(24 * 60 * 60 * 1000)
    }
    override fun getUrl() = apiUrl
}