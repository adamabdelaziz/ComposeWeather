package com.example.composeweather.repository

import com.example.composeweather.domain.dao.WeatherDao
import com.example.composeweather.network.WeatherRemoteDataSource
import com.example.composeweather.util.performGetOperation
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherDao
) {

    //Probably will have to change the function parameters in the Dao
    fun getCurrentOneCall(lat: Long, lon:Long) = performGetOperation(
        databaseQuery = {localDataSource.getCurrentOneCall()},
        networkCall = {remoteDataSource.getOneCallLatLon(lat,lon)},
        saveCallResult = {localDataSource.insertOneCall(it)}
    )
}