package com.micker.home.api

import com.micker.data.constant.BASE_URL
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class SvgUserDeleteApi(listener : ResponseListener<String>, val id : String) : CustomJsonApi<String>(listener){
    override fun getUrl() = "${BASE_URL}svg/user/delete"

    override fun getRequestJSONBody(): JSONObject {
        val jobj = JSONObject()
        jobj.put("id", id)
        return jobj;
    }
}