package com.example.composeweather.util

import androidx.room.TypeConverter
import com.example.composeweather.domain.model.*
import com.google.gson.Gson
import timber.log.Timber

class Converters {

    //FeelsLike
    @TypeConverter
    fun feelsLikeToString(feelsLike: FeelsLike): String = Gson().toJson(feelsLike)

    @TypeConverter
    fun stringToFeelsLike(string: String?): FeelsLike? =
        Gson().fromJson(string, FeelsLike::class.java)

    //Current
    @TypeConverter
    fun currentToString(current: Current): String = Gson().toJson(current)

    @TypeConverter
    fun stringToCurrent(string: String?): Current? = Gson().fromJson(string, Current::class.java)

    //Daily
    @TypeConverter
    fun dailyToString(daily: Daily?): String? = Gson().toJson(daily)

    @TypeConverter
    fun stringToDaily(string: String?): Daily? = Gson().fromJson(string, Daily::class.java)

    //Hourly
    @TypeConverter
    fun hourlyToString(hourly: Hourly?): String? = Gson().toJson(hourly) ?: ""

    @TypeConverter
    fun stringToHourly(string: String?): Hourly? = Gson().fromJson(string, Hourly::class.java)

    //Minutely
    @TypeConverter
    fun minutelyToString(minutely: Minutely?): String? = Gson().toJson(minutely) ?: ""

    @TypeConverter
    fun stringToMinutely(string: String?): Minutely? = Gson().fromJson(string, Minutely::class.java)

    //List Daily
    @TypeConverter
    fun dailyListToString(value: List<Daily>?) = Gson().toJson(value)

    @TypeConverter
    fun stringToDailyList(value: String) = Gson().fromJson(value, Array<Daily>::class.java).toList()

    //List Hourly
    @TypeConverter
    fun hourlyListToString(value: List<Hourly>?) = Gson().toJson(value)

    @TypeConverter
    fun stringToHourlyList(value: String) =
        Gson().fromJson(value, Array<Hourly>::class.java).toList()

    //ListMinutely
    @TypeConverter
    fun minutelyListToString(value: List<Minutely>?) = Gson().toJson(value)

    @TypeConverter
    fun stringToMinutelyList(value: String?) =
        Gson().fromJson(value, Array<Minutely>::class.java).toList()


    //ListAlert
    @TypeConverter
    fun alertListToString(value: List<Alert>?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToAlertList(value: String?): List<Alert>? {
        Timber.d(value + " value")
        return if (value == "null") {
            null
        } else {
            Gson().fromJson(value, Array<Alert>::class.java).toList() ?: emptyList()
        }

    }


    //Alert
    @TypeConverter
    fun alertToString(alert: Alert?): String? = Gson().toJson(alert) ?: ""

    @TypeConverter
    fun stringToAlert(string: String?): Alert? = Gson().fromJson(string, Alert::class.java)

}