package com.micker.seven.const

import com.micker.helper.file.CacheUtils
import com.micker.seven.model.SevenModelEnitity
import org.json.JSONArray

val list by lazy {
    val stream = CacheUtils.getFileFromAssets("seven/poetry_database.json")
    val json = CacheUtils.InputStreamToString(stream)
    val jarr = JSONArray(json)
    val length = jarr.length()
    val list = ArrayList<SevenModelEnitity>()
    (0 until length)?.forEach {
        val entity = SevenModelEnitity()
        val jobj = jarr.optJSONArray(it)
        entity.title = jobj.opt(0).toString()
        entity.author = jobj.opt(1).toString()
        entity.content = jobj.opt(2).toString()
        entity.tranlate = jobj.opt(3).toString()
        entity.id = jobj.opt(4).toString().toInt()
        list.add(entity)
    }
    list
}