package com.adamcodem.composeweather.network

import com.adamcodem.composeweather.domain.model.OneCall
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    //One Call API
    //Units=Imperial is Farenheit
    //May be Long or Double have to see how to get coordinates in Android first
    // Either @Query or @Path
    @GET("onecall?")
    suspend fun getOneCallLatLon(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid : String,
        @Query("units") units:String
    ): OneCall

    @GET("onecall?")
    suspend fun getOneCallLatLonResponse(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid : String,
        @Query("units") units:String
    ): Response<OneCall>
}