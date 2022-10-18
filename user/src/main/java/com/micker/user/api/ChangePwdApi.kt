package com.micker.user.api

import com.micker.data.constant.BASE_URL
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class ChangePwdApi(val mobile : String?, val code : String?, val pwd : String?, listener : ResponseListener<String>) : CustomJsonApi<String>(listener) {
    override fun getUrl() = "${BASE_URL}user/phone_chagne_pwd"

    override fun getRequestJSONBody(): JSONObject {
        val jobj = JSONObject()
        jobj.put("code", code)
        jobj.put("phone", mobile)
        jobj.put("password", pwd)
        return jobj
    }
}