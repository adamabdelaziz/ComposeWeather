package com.example.composeweather.network

import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    //One Call API
    //Units=Imperial is Farenheit
    //May be Long or Double have to see how to get coordinates in Android first
    @GET("/onecall?lat={lat}&lon={lon}&units=imperial&appid=$API_KEY")
    suspend fun getOneCallLatLon(
        @Path("lat") lat: Long,
        @Path("lon") lon: Long
    ): Response<OneCall>
}