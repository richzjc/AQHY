package com.wallstreetcn.quotes.stocksearch.room

import com.micker.seven.db.SevenModelEnitity

class StockDbResposity(val stockDao: PoetryDao?){

    fun query(): List<SevenModelEnitity>?{
       return stockDao?.query()
    }
}