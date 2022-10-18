package com.micker.data.model.home

class ColorCategoryEntity {
    var type : ColorTypeEntity? = ColorTypeEntity.MaterialTypeSolidColor
    var name : String? = ""
    var color : String? = ""
    var image : String? = ""
    var webColors : Array<String>? = null
}