package com.example.composeweather.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "OneCall")
data class OneCall(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val minutely: List<Minutely>,
    val alerts: List<Alert>,
    val timezone: String,
    @SerializedName("timezone_offset")
    val offset: Double? = 0.0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}