package com.micker.home.api

import android.os.Bundle
import com.kronos.volley.toolbox.BaseApiParser
import com.micker.data.constant.BASE_URL
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.global.api.BaseListModel
import com.micker.global.api.BaseListModelParser
import com.micker.global.api.BaseListResponse
import com.micker.rpc.CustomJsonApi
import org.json.JSONObject

class HomeCategoryListApi(listener : BaseListResponse<BaseListModel<HomeSubItemEntity>>, bundle: Bundle) : CustomJsonApi<BaseListModel<HomeSubItemEntity>>(listener, bundle){

    override fun getUrl() = "${BASE_URL}svg/group/svg/all"

    override fun getRequestJSONBody(): JSONObject {
        val groupId = bundle.getString("groupId", "")
        val page = bundle.getInt("page", 1)
        val jobj = JSONObject()
        jobj.put("groupId", groupId)
        jobj.put("page", page)
        jobj.put("pageSize", 20)
        return jobj
    }

    override fun getParser(): BaseApiParser {
       return  BaseListModelParser(HomeSubItemEntity::class.java)
    }
}