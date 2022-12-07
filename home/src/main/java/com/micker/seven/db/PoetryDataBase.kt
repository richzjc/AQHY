package com.wallstreetcn.quotes.stocksearch.room

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import com.micker.seven.db.SevenModelEnitity

@Database(entities = [SevenModelEnitity::class], version = 1, exportSchema = false)
abstract class PoetryDataBase : RoomDatabase(){

    abstract fun stockDao(): PoetryDao

    companion object {
        @Volatile
        private var INSTANCE: PoetryDataBase? = null

        fun getDatabase(context: Context): PoetryDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PoetryDataBase::class.java,
                    "poetry"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
