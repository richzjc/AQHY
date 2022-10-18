package com.micker.home.callback

import android.os.Bundle
import com.micker.data.model.home.ColorCategoryEntity
import com.micker.data.model.home.ColorGroupDetailEntity
import com.micker.data.model.home.StatusActionEntity


interface SvgDetailCallback {
    fun loadSvgSucc(svgPath : String?, callbackId : String?)
    fun loadSvgError()
    fun loadColorCategory(list : ArrayList<ColorCategoryEntity>)
    fun loadColorList(entity: ColorGroupDetailEntity?)
    fun statusAction(entity: StatusActionEntity?)
    fun uploadError()
    fun uploadSuccess(bundle : Bundle)
}