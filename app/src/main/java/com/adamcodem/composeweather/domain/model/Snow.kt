package com.adamcodem.composeweather.domain.model

import com.google.gson.annotations.SerializedName

data class Snow(
    @SerializedName("1h")
    val oneHour: Double = 0.0,
    @SerializedName("3h")
    val threeHour: Double = 0.0
)