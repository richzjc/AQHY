package com.micker.data.model.home

class ColorGroupDetailEntity {
    var titles: List<String?>? = null
    var colorList: List<ColorGroupByTypeEntity>? = null

    companion object {
        fun packageData(datas: List<ColorGroupByTypeEntity>?): ColorGroupDetailEntity {
            val entity = ColorGroupDetailEntity()
            val titles = ArrayList<String>()
            datas?.forEach {
                titles.add(it.name!!)
            }
            entity.titles = titles
            entity.colorList = datas
            return entity
        }
    }

    fun getColorListData(selectPosition: Int): List<ColorsItem>? {
        if (colorList == null || selectPosition >= colorList!!.size)
            return ArrayList()
        else{
            return  colorList!![selectPosition].colors
        }
    }
}