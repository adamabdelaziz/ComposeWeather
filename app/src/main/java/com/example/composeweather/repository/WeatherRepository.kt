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

            //Timber.d(oneCall.timezone + "timeZone")
            //Timber.d("${oneCall.current}" + " Current")
            //Timber.d("${oneCall.alerts}" + " Alerts")
            //Timber.d(lat)
           // Timber.d(lon)

            localDataSource.insertOneCall(oneCall)
            return oneCall
        } else {
            Timber.d("ONECALL IS NULL, GETTING FROM ROOOM")
            return localDataSource.getCurrentOneCall()
        }
    }

//    @SuppressLint("MissingPermission")
//    suspend fun getLocation(fusedLocationProviderClient: FusedLocationProviderClient): Coord {
//
//        //See why coord value stays as 3.0 and doesnt change thus causing crash
//        var coord: Coord = Coord(NYC_LAT.toDouble(), NYC_LON.toDouble())
//
//        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
//            Timber.d("Does it even fucking get here honestly")
//            if (location == null) {
//                Timber.d("Location null naniiiii")
//                coord = Coord(NYC_LAT.toDouble(), NYC_LON.toDouble())
//            } else {
//                Timber.d("Location isnt? null naniiiii")
//                Timber.d(location.latitude.toString())
//                Timber.d(location.longitude.toString())
//                coord = Coord(location.latitude, location.longitude)
//            }
//        }
//
//        return coord
//    }
}