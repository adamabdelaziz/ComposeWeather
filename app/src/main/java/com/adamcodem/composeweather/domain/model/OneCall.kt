package com.adamcodem.composeweather.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adamcodem.composeweather.util.FAHRENHEIT
import com.google.gson.annotations.SerializedName

@Entity(tableName = "OneCall")
data class OneCall(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val minutely: List<Minutely>,
    val alerts: List<Alert>?,
    val timezone: String,
    @SerializedName("timezone_offset")
    val offset: Double? = 0.0
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 1
    var unit: String? = FAHRENHEIT
}