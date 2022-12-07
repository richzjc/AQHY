package com.wallstreetcn.quotes.stocksearch.room

import androidx.lifecycle.ViewModel
import com.micker.seven.db.SevenModelEnitity
import kotlinx.coroutines.launch

class PoetryDbViewModel : ViewModel(){

    var responsebility : StockDbResposity? = null

    val stockList = query()

    fun query(): List<SevenModelEnitity>?{
        return responsebility?.query()
    }
}