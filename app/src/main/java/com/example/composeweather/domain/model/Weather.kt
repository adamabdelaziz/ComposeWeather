package com.example.composeweather.domain.model

//https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)