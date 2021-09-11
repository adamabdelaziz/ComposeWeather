package com.adamcodem.composeweather.util

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.adamcodem.composeweather.ui.theme.Grey200
import timber.log.Timber
import java.lang.Math.floor
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

const val API_KEY = "e8e98e12bdbf52acbf23acc3257c613a"

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

const val FAHRENHEIT: String = "IMPERIAL"

const val CELSIUS: String = "METRIC"

const val HOUR_IN_MILLISECONDS : Long = 3600000

const val NYC_LAT = 40.7131.toString()

const val NYC_LON = (-74.0072).toString()

//const val NYC_LAT = 42.3601.toString()
//
//const val NYC_LON = 71.0589.toString()

const val DEGREE_SYMBOL = "\u00B0"


const val RAIN_ICON_DAY = "https://openweathermap.org/img/wn/10d@4x.png"

const val RAIN_ICON_NIGHT = "https://openweathermap.org/img/wn/10n@4x.png"

const val SNOW_ICON_DAY = "https://openweathermap.org/img/wn/13d@4x.png"

const val SNOW_ICON_NIGHT = "https://openweathermap.org/img/wn/13n@4x.png"

const val FEW_CLOUDS_NIGHT = "https://openweathermap.org/img/wn/02n@4x.png"

const val SCATTERED_CLOUDS_NIGHT = "https://openweathermap.org/img/wn/03n@4x.png"

const val OVERCAST_CLOUDS_NIGHT = "https://openweathermap.org/img/wn/04n@4x.png"

const val SECONDS_IN_A_DAY = 86400

//These two get weather icons from the API
fun getIconSmall(iconId: String): String {
    return "https://openweathermap.org/img/wn/$iconId@2x.png"
}

fun getIconLarge(iconId: String): String {
    return "https://openweathermap.org/img/wn/$iconId@4x.png"
}

//mm to inches for rainfall/snowfall
fun toInches(measurement: Double): String {
    return measurement.times(0.0394).toString().substring(0, 4)
}

fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())

    return (this * factor).roundToInt() / factor
}

// Gets the day of the week from the UnixTime and Offset
fun getDayFromUnix(unixTime: Double, offset: Double): String {
    val time = unixTime - offset
    val div = floor(time.div(SECONDS_IN_A_DAY).toDouble())
    val result = div.rem(7)

    return when (result) {
        0.0 -> "Thu"
        1.0 -> "Fri"
        2.0 -> "Sat"
        3.0 -> "Sun"
        4.0 -> "Mon"
        5.0 -> "Tue"
        6.0 -> "Wed"
        else -> "Nani??"
    }
}
val Colors.snackbarAction: Color
    @Composable get() = Grey200
fun getTimestampFromUnix(unixTime: Double, offset: Double): String {
    val time = unixTime.plus(offset)
    val simpleDateFormat = SimpleDateFormat("hh a", Locale.ENGLISH)

    val string = java.time.format.DateTimeFormatter.ISO_INSTANT
        .format(java.time.Instant.ofEpochSecond(time.toLong()))

    Timber.d(string + "kotlinNew")

    var timeStamp = simpleDateFormat.format(time * 1000L)

    return if(timeStamp.startsWith("0")){
        timeStamp = timeStamp.substring(1)
        timeStamp
    }
    else
        timeStamp


}
