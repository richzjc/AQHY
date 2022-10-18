package com.micker.home.singleton

import com.micker.data.model.home.ColorTypeEntity
import com.micker.data.model.home.ColorsItem
import com.micker.helper.observer.ObserverIds
import com.micker.helper.observer.ObserverManger
import com.micker.webview.Widget.WSCNWebView
import com.micker.webview.Widget.WebViewCompat
import org.json.JSONArray
import org.json.JSONObject

object ColorsItemFactory {
    val whiteColorsItem by lazy {
        val item = ColorsItem()
        item.color = "ffffff"
        item.name = "白色"
        item.type = ColorTypeEntity.MaterialTypeSolidColor.type
        item
    }

    var leftColorsItem: ColorsItem? = null
    var rightsColorsItem: ColorsItem? = null

    fun init() {
        rightsColorsItem = whiteColorsItem
        leftColorsItem = whiteColorsItem
    }

    fun clear() {
        leftColorsItem = null
        rightsColorsItem = null
    }

    fun selectColorsItem(colorsItem: ColorsItem) {
        if (leftColorsItem != colorsItem) {
            rightsColorsItem = leftColorsItem
            leftColorsItem = colorsItem
            ObserverManger.getInstance().notifyObserver(ObserverIds.COLORS_ITEM_SELECTED)
        }
    }

    fun loadFill(view : WSCNWebView?){
        view?.let {
            leftColorsItem?.apply {
                when(type){
                    ColorTypeEntity.MaterialTypeSolidColor.type -> responseSolid(view)
                    ColorTypeEntity.MaterialTypeLinearColor.type -> responseLinear(view)
                    ColorTypeEntity.MaterialTypeGradialColor.type -> responseGradial(view)
                    ColorTypeEntity.MaterialTypePattern.type -> responsePatter(view)
                    ColorTypeEntity.MaterialTypeFilter.type -> responseFilter(view)
                }
            }
        }
    }

    private fun responseSolid(view : WSCNWebView){
        val resultObject = JSONObject()
        resultObject.put("color", "#${leftColorsItem!!.color}")
        resultObject.put("type", leftColorsItem!!.type)
        val jsonObject = JSONObject()
        jsonObject.put("data", resultObject)
        val value = jsonObject.toString()
        WebViewCompat.loadJsFunction(view, "window.__YutaFillContent($value);")
    }

    private fun responseLinear(view :WSCNWebView){
        val resultObject = JSONObject()
        resultObject.put("type", leftColorsItem!!.type)
        resultObject.put("direction", leftColorsItem!!.direction)
        val list = leftColorsItem!!.color!!.split(",")
        val list2 = list.map { "#${it}"}
        val jsonArray = JSONArray()
        list2.forEach { jsonArray.put(it) }
        resultObject.put("colors", jsonArray)
        val jsonObject = JSONObject()
        jsonObject.put("data", resultObject)
        val value = jsonObject.toString()
        WebViewCompat.loadJsFunction(view, "window.__YutaFillContent($value);")
    }

    private fun responseGradial(view :WSCNWebView){
        val resultObject = JSONObject()
        resultObject.put("type", leftColorsItem!!.type)
        val list = leftColorsItem!!.color!!.split(",")
        val list2 = list.map { "#${it}"}
        val jsonArray = JSONArray()
        list2.forEach { jsonArray.put(it) }
        resultObject.put("colors", jsonArray)
        val jsonObject = JSONObject()
        jsonObject.put("data", resultObject)
        val value = jsonObject.toString()
        WebViewCompat.loadJsFunction(view, "window.__YutaFillContent($value);")
    }

    private fun responsePatter(view :WSCNWebView){
        val resultObject = JSONObject()
        resultObject.put("type", leftColorsItem!!.type)
        resultObject.put("content", leftColorsItem!!.image)
        resultObject.put("width", leftColorsItem!!.width)
        resultObject.put("height", leftColorsItem!!.height)
        resultObject.put("patid", "mic-${leftColorsItem!!.filterId}")
        val jsonObject = JSONObject()
        jsonObject.put("data", resultObject)
        val value = jsonObject.toString()
        WebViewCompat.loadJsFunction(view, "window.__YutaFillContent($value);")
    }

    private fun responseFilter(view :WSCNWebView){
        val resultObject = JSONObject()
        resultObject.put("type", leftColorsItem!!.type)
        resultObject.put("patid", "${leftColorsItem!!.filterId}")
        resultObject.put("content", leftColorsItem!!.content)
        val jsonObject = JSONObject()
        jsonObject.put("data", resultObject)
        val value = jsonObject.toString()
        WebViewCompat.loadJsFunction(view, "window.__YutaFillContent($value);")
    }
}