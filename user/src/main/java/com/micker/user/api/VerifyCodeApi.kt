package com.micker.user.api

import com.micker.data.constant.BASE_URL
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class VerifyCodeApi(val mobile : String, listener : ResponseListener<String>) : CustomJsonApi<String>(listener) {
    override fun getUrl() = "${BASE_URL}sms/verifycode"

    override fun getRequestJSONBody(): JSONObject {
        val jobj = JSONObject()
        jobj.put("type", "102")
        jobj.put("mobile", mobile)
        return jobj
    }
}