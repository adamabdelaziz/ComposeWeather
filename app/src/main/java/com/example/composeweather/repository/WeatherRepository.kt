package com.example.composeweather.repository

import com.example.composeweather.domain.dao.WeatherDao
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.network.WeatherService
import com.example.composeweather.util.API_KEY
import com.example.composeweather.util.FAHRENHEIT
import timber.log.Timber
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherService,
    private val localDataSource: WeatherDao
) {

    suspend fun getOneCall(lat: String, lon: String): OneCall {
        val oneCall = remoteDataSource.getOneCallLatLon(lat, lon, API_KEY, FAHRENHEIT)

        if (oneCall != null) {
            Timber.d("ONECALL NOT NULL")

            Timber.d(oneCall.timezone + "timeZone")
            Timber.d("${oneCall.current}" + " Current")
            Timber.d("${oneCall.alerts}" + " Alerts")

            localDataSource.insertOneCall(oneCall)
            return oneCall
        } else {
            Timber.d("ONECALL IS NULL, GETTING FROM ROOOM")
            return localDataSource.getCurrentOneCall()
        }
    }
}