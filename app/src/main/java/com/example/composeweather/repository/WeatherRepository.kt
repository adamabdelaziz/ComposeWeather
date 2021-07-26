package com.example.composeweather.repository

import android.annotation.SuppressLint
import android.location.Location
import com.example.composeweather.domain.dao.WeatherDao
import com.example.composeweather.domain.model.Coord
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.network.WeatherService
import com.example.composeweather.util.API_KEY
import com.example.composeweather.util.FAHRENHEIT
import com.example.composeweather.util.NYC_LAT
import com.example.composeweather.util.NYC_LON
import com.google.android.gms.location.FusedLocationProviderClient
import timber.log.Timber
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherService,
    private val localDataSource: WeatherDao,
) {

    suspend fun getOneCall(lat: String, lon: String): OneCall {
        val oneCall = remoteDataSource.getOneCallLatLon(lat, lon, API_KEY, FAHRENHEIT)

        if (oneCall != null) {
            Timber.d("ONECALL NOT NULL")
            localDataSource.insertOneCall(oneCall)
            return oneCall
        } else {
            Timber.d("ONECALL IS NULL, GETTING FROM ROOOM")
            return localDataSource.getCurrentOneCall()
        }
    }
}