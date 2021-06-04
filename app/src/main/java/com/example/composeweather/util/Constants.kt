package com.example.composeweather.util

const val API_KEY = "e8e98e12bdbf52acbf23acc3257c613a"

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

const val FAHRENHEIT = "IMPERIAL"

const val NYC_LAT = 40.7131.toString()

const val NYC_LON = (-74.0072).toString()

const val DEGREE_SYMBOL = "\u00B0"


const val RAIN_ICON_DAY = "https://openweathermap.org/img/wn/10d@4x.png"

const val RAIN_ICON_NIGHT = "https://openweathermap.org/img/wn/10n@4x.png"

const val SNOW_ICON_DAY = "https://openweathermap.org/img/wn/13d@4x.png"

const val SNOW_ICON_NIGHT = "https://openweathermap.org/img/wn/13n@4x.png"

const val FEW_CLOUDS_NIGHT = "https://openweathermap.org/img/wn/02n@4x.png"

const val SCATTERED_CLOUDS_NIGHT = "https://openweathermap.org/img/wn/03n@4x.png"

const val OVERCAST_CLOUDS_NIGHT = "https://openweathermap.org/img/wn/04n@4x.png"

fun getIconSmall(iconId: String): String {
    return "http://openweathermap.org/img/wn/$iconId@2x.png"
}

fun getIconLarge(iconId: String): String {
    return "http://openweathermap.org/img/wn/$iconId@4x.png"
}

fun toInches(measurement: Double): Double {
    return measurement.div(25.4)
}