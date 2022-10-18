package com.micker.push

import android.os.Bundle
import com.micker.data.constant.BASE_URL
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class UpdatePushTokenApi(bundle: Bundle?, val userId : Long?, listener : ResponseListener<String>?) : CustomJsonApi<String>(listener, bundle) {
    override fun getUrl() = "${BASE_URL}user/updatePushToken"
    override fun getRequestJSONBody(): JSONObject {
        val obj =  super.getJsonFromBundle()
        obj.put("userId", userId)
        obj.put("sysNotification", 1)
        return obj;
    }
}