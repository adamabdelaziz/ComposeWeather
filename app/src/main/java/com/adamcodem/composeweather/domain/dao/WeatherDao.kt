package com.adamcodem.composeweather.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adamcodem.composeweather.domain.model.OneCall

@Dao
interface WeatherDao {

    @Query("SELECT * FROM OneCall")
    suspend fun getCurrentOneCall(): OneCall

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneCall(oneCall : OneCall)


    @Query("SELECT * FROM OneCall")
    fun getCurrentOneCallAsLiveData() : LiveData<OneCall>
}