package com.wallstreetcn.quotes.stocksearch.room

import androidx.room.Dao
import androidx.room.Query
import com.micker.seven.db.SevenModelEnitity

@Dao
interface PoetryDao {

    @Query(value = "SELECT * FROM poetry")
    fun query(): List<SevenModelEnitity>?
}