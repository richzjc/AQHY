package com.wallstreetcn.quotes.stocksearch.room

import androidx.lifecycle.ViewModel
import com.micker.seven.db.SevenModelEnitity

class PoetryDbViewModel : ViewModel(){

    var responsebility : PoetryDbResposity? = null

    val stockList = query()

    fun query(): List<SevenModelEnitity>?{
        return responsebility?.query()
    }
}