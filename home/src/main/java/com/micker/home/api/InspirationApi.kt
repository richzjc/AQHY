package com.micker.home.api

import android.os.Bundle
import com.micker.data.constant.BASE_URL
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.global.api.BaseListModel
import com.micker.global.api.BaseListModelParser
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class InspirationApi(listener: ResponseListener<BaseListModel<InspirationEntity>>, bundle : Bundle?) : CustomJsonApi<BaseListModel<InspirationEntity>>(listener, bundle){
    override fun getUrl() = "${BASE_URL}svg/user/linggan"
    override fun getRequestJSONBody(): JSONObject {

        val jobj = JSONObject()
        val page = bundle?.getInt("page", 1) ?: 1
        jobj.put("page", page)
        jobj.put("pageSize", 20)
        return jobj
    }

    override fun getParser() = BaseListModelParser(InspirationEntity::class.java)
}