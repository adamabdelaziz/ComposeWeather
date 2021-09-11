package com.adamcodem.composeweather.util

sealed class OneCallEvent {
    object RestoreStateEvent : OneCallEvent()
    class RefreshWeather(val lat : String, val lon: String) : OneCallEvent()
    class UpdateLocation(val locationSetting: Boolean) : OneCallEvent()
}