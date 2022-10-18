package com.micker.user.api

import com.micker.data.constant.BASE_URL
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class UserRegistApi(val mobile : String, val code : String, val pwd : String?, val name : String?, listener : ResponseListener<String>) : CustomJsonApi<String>(listener){

    override fun getUrl() = "${BASE_URL}user/register"

    override fun getRequestJSONBody(): JSONObject {
        val jobj = JSONObject()
        jobj.put("password", pwd)
        jobj.put("phone", mobile)
        jobj.put("name", name)
        jobj.put("code", code)
        jobj.put("b2bapp", true)
        return jobj
    }

}