package com.adamcodem.composeweather.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adamcodem.composeweather.domain.dao.WeatherDao
import com.adamcodem.composeweather.domain.model.OneCall
import com.adamcodem.composeweather.util.Converters

@Database(entities = [OneCall::class], version = 13, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "weatherdb")
                .fallbackToDestructiveMigration()
                .build()
    }

}