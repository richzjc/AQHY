package com.wallstreetcn.quotes.stocksearch.room

import com.micker.seven.db.SevenModelEnitity

class PoetryDbResposity(val stockDao: PoetryDao?){

    fun query(): List<SevenModelEnitity>?{
       return stockDao?.query()
    }
}