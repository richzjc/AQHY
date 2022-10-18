package com.micker.global.api

import android.os.Parcelable
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.kronos.volley.ParseError
import com.kronos.volley.toolbox.BaseApiParser
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.global.user.AccountManager

class BaseListModelParser<T : Parcelable>(val mClass: Class<T>) : BaseApiParser {

    @Throws(ParseError::class)
    override fun parse(content: String): Any? {
        val listModel = BaseListModel<T>()
        listModel.addAll(JSON.parseArray<T>(content, mClass))

        if(TextUtils.equals(mClass.simpleName, "InspirationEntity")){
            listModel.forEach {
                (it as? InspirationEntity)?.also { inner ->
                    inner.isLogined = AccountManager.isLogin()
                }
            }
        }
        return listModel
    }
}