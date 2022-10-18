package com.micker.home.singleton

import com.micker.data.model.home.ColorsItem


object ColorHistoryFactory {
    val historyData: MutableList<ColorsItem?> = mutableListOf()

    fun clear(){
       historyData.clear()
    }

    fun put(colorsItem : ColorsItem?){
        if(historyData.size >= 5){
            historyData.removeAt(4)
        }
        historyData.add(0, colorsItem)
    }
}