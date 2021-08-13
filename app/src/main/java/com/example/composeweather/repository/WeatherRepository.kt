package com.example.composeweather.repository

import com.example.composeweather.domain.dao.WeatherDao
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.network.WeatherRemoteDataSource
import com.example.composeweather.util.performGetOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherDao,
) {

    suspend fun getOneCall(lat: String, lon: String, unit: String): OneCall {
        val oneCall = remoteDataSource.getOneCallLatLon(lat, lon, unit)

        if (oneCall != null) {
            Timber.d("ONECALL NOT NULL")
            localDataSource.insertOneCall(oneCall)
            return oneCall
        } else {
            Timber.d("ONECALL IS NULL, GETTING FROM ROOOM")
            return localDataSource.getCurrentOneCall()
        }
    }

    fun getOneCallResponse(lat: String, lon: String, unit: String) = performGetOperation(
        databaseQuery = { localDataSource.getCurrentOneCallAsLiveData() },
        networkCall = { remoteDataSource.getOneCallLatLonResponse(lat, lon, unit) },
        saveCallResult = { localDataSource.insertOneCall(it) }
    )

    fun getOneCallFlow(lat: String, lon: String, unit: String): Flow<OneCall> = flow {
        emit(remoteDataSource.getOneCallLatLon(lat, lon, unit))
    }.flowOn(Dispatchers.IO)
}