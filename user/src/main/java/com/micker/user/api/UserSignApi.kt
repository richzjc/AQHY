package com.micker.user.api
import com.micker.data.constant.BASE_URL
import com.micker.data.model.user.AccountUserInfoEntity
import com.micker.rpc.CustomJsonApi
import com.micker.rpc.GsonApiParser
import com.micker.rpc.ResponseListener
import org.json.JSONObject

class UserSignApi(val mobile : String, val code : String, listener : ResponseListener<AccountUserInfoEntity>) : CustomJsonApi<AccountUserInfoEntity>(listener){
    override fun getUrl() = "${BASE_URL}user/login"

    override fun getRequestJSONBody(): JSONObject {
        val jobj = JSONObject()
        jobj.put("password", code)
        jobj.put("phone", mobile)
        return jobj
    }

    override fun getParser() = GsonApiParser(AccountUserInfoEntity::class.java)
}