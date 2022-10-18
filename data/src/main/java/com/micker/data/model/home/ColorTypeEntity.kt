package com.micker.data.model.home

enum class ColorTypeEntity(val type : Int) {
    MaterialTypeSolidColor(0),
    MaterialTypeLinearColor(10),
    MaterialTypeGradialColor(20),
    MaterialTypePattern(30),
    MaterialTypeFilter(40);
}