package com.adamcodem.composeweather.domain.model

data class Main(
    val feels_like: FeelsLike,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)