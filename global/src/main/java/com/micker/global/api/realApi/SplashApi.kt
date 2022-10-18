package com.micker.global.api.realApi

import com.alibaba.fastjson.JSON
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.constant.BASE_URL
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.GsonApiParser
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class SplashApi(val key : String?, val resultKey : String, listener : ResponseListener<List<LaunchConfigEntity>>?) : CustomJsonApi<List<LaunchConfigEntity>>(listener) {

    override fun getUrl() = "${BASE_URL}app/config/getkey"

    override fun getRequestJSONBody(): JSONObject {
        val jobj = JSONObject()
        jobj.put("key", key)
        return jobj
    }

    override fun getParser() = SplashParser(LaunchConfigEntity::class.java, resultKey)


    class SplashParser(cls : Class<*>, val resultKey: String) : GsonApiParser(cls){
        override fun parse(content: String?): List<LaunchConfigEntity> {
            val list = ArrayList<LaunchConfigEntity>()
            val jobj = JSONObject(content)
            val result = jobj.optString(resultKey, "")
            val temp = JSON.parseArray(result, LaunchConfigEntity::class.java)
            if(temp != null && temp.isNotEmpty())
                list.addAll(temp)
            return list
        }
    }
}