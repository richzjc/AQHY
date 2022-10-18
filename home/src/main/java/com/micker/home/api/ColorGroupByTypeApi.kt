package com.micker.home.api

import android.os.Bundle
import com.micker.data.constant.BASE_URL
import com.micker.data.model.home.ColorGroupByTypeEntity
import com.micker.rpc.CustomGsonApiListParser
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class ColorGroupByTypeApi(listener: ResponseListener<List<ColorGroupByTypeEntity>>, bundle: Bundle) : CustomJsonApi<List<ColorGroupByTypeEntity>>(listener, bundle) {

    override fun getUrl() = "${BASE_URL}color/group/all/type"

    override fun getParser() = CustomGsonApiListParser(ColorGroupByTypeEntity::class.java)

    override fun getRequestJSONBody(): JSONObject {
        val jobj = JSONObject()
        jobj.put("page", bundle.getInt("page", 1))
        jobj.put("pageSize", bundle.getInt("pageSize", 50))
        jobj.put("type", bundle.getInt("type", 0))
        return jobj
    }
}