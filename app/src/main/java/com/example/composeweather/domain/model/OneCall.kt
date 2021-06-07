package com.example.composeweather.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.composeweather.domain.model.Current
import com.example.composeweather.domain.model.Daily
import com.example.composeweather.domain.model.Hourly
import com.example.composeweather.domain.model.Minutely
import com.google.gson.annotations.SerializedName

@Entity(tableName = "OneCall")
data class OneCall(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val minutely: List<Minutely>,
    val timezone: String,
    @SerializedName("timezone_offset")
    val offset: Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}