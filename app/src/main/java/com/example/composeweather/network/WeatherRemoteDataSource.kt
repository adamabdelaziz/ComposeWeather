package com.example.composeweather.network

import com.example.composeweather.util.API_KEY
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(private val weatherService: WeatherService) :
    BaseDataSource() {

    suspend fun getOneCallLatLonResponse(lat: String, lon: String, unit: String) =
        getResult { weatherService.getOneCallLatLonResponse(lat, lon, API_KEY, unit) }

    suspend fun getOneCallLatLon(lat: String, lon: String, unit: String) =
        weatherService.getOneCallLatLon(
            lat, lon,
            API_KEY, unit
        )

}