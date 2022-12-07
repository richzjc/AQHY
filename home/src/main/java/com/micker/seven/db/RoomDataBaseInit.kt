package com.micker.seven.db

import com.micker.helper.UtilsContextManager
import com.wallstreetcn.quotes.stocksearch.room.PoetryDao
import com.wallstreetcn.quotes.stocksearch.room.PoetryDataBase
import com.wallstreetcn.quotes.stocksearch.room.PoetryDbResposity

object RoomDataBaseInit {
    var dataBase : PoetryDataBase? = null
    var stockDao  : PoetryDao? = null
    var responsebility : PoetryDbResposity? = null

    init {
        dataBase = PoetryDataBase.getDatabase(UtilsContextManager.getInstance().application)
        stockDao = dataBase?.stockDao()
        responsebility = PoetryDbResposity(stockDao)
    }
}