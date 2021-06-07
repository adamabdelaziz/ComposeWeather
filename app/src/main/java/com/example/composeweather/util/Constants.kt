package com.example.composeweather.util

import timber.log.Timber
import java.lang.Math.floor

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

const val EXPAND_ANIMATION_DURATION = 300

const val COLLAPSE_ANIMATION_DURATION = 300

const val FADE_IN_ANIMATION_DURATION = 300

const val FADE_OUT_ANIMATION_DURATION = 300

const val SECONDS_IN_A_DAY = 86400

//These two get weather icons from the API
fun getIconSmall(iconId: String): String {
    return "http://openweathermap.org/img/wn/$iconId@2x.png"
}

fun getIconLarge(iconId: String): String {
    return "http://openweathermap.org/img/wn/$iconId@4x.png"
}
//mm to inches for rainfall/snowfall
fun toInches(measurement: Double): Double {
    return measurement.div(25.4)
}
// Gets the day of the week from the UnixTime and Offset
fun getDayFromUnix(unixTime: Int, offset: Int): String {
    val time = unixTime - offset
    Timber.d("$time is time")
    val div = floor(time.div(SECONDS_IN_A_DAY).toDouble())
    Timber.d("$div is div")
    val result = div.rem(7)
    Timber.d("$result is result")
    return when (result) {
        0.0 -> "Thursday"
        1.0 -> "Friday"
        2.0 -> "Saturday"
        3.0 -> "Sunday"
        4.0 -> "Monday"
        5.0 -> "Tuesday"
        6.0 -> "Wednesday"
        else -> "Nani??"
    }
}