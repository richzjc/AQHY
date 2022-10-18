package com.micker.home.api

import android.os.Bundle
import android.text.TextUtils
import com.micker.data.constant.BASE_URL
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.global.user.AccountManager.getAccountUserId
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class SvgUserUpdateApi(
    listener: ResponseListener<String>,
    val mBundle: Bundle?,
    val status: Int
) : CustomJsonApi<String>(listener) {

    override fun getUrl() = "${BASE_URL}svg/user/update"

    override fun getRequestJSONBody(): JSONObject {
        val contentUrl = mBundle?.getString("contentUrl", "")
        val imageUrl = mBundle?.getString("imageUrl", "")
        val entity = mBundle?.getParcelable<HomeSubItemEntity>("entity")
        val jobj = JSONObject()
        jobj.put("userId", getAccountUserId())
        jobj.put("svgId", entity?.svgId)
        jobj.put("image", imageUrl)
        jobj.put("svgUrl", contentUrl)
        jobj.put("state", status)
        jobj.put("content", "")
        if (!TextUtils.equals(entity?.svgId, entity?.id) && !TextUtils.isEmpty(entity?.id))
            jobj.put("id", entity?.id)
        else
            jobj.put("id", "")
        return jobj
    }
}