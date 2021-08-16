package com.example.composeweather.repository

import com.example.composeweather.domain.dao.WeatherDao
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.network.WeatherRemoteDataSource
import com.example.composeweather.util.HOUR_IN_MILLISECONDS
import timber.log.Timber
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherDao,
) {
    suspend fun insertOneCall(oneCall: OneCall) {
        Timber.d("insertOneCall() called")
        localDataSource.insertOneCall(oneCall)
    }


    suspend fun getRemoteOneCall(lat: String, lon: String, unit: String): OneCall {
        val oneCall = remoteDataSource.getOneCallLatLon(lat, lon, unit)
        Timber.d("getRemoteOneCall() called")
        if (oneCall != null) {
            Timber.d("ONECALL NOT NULL")
            oneCall.unit = unit
            insertOneCall(oneCall)
            return oneCall
        } else {
            Timber.d("ONECALL IS NULL, GETTING FROM ROOOM")
            return localDataSource.getCurrentOneCall()
        }
    }

    suspend fun getLocalOneCall(): OneCall {
        Timber.d("getLocalOneCall() called")
        return localDataSource.getCurrentOneCall()
    }

    suspend fun getCorrectOneCall(lat: String, lon: String, unit: String): OneCall {
        val localCall = getLocalOneCall()
        Timber.d(unit + " unit RepositoryValue")

        if (localCall != null) {
            val localUnit = localCall.unit
            Timber.d(localUnit + " localUnit RepositoryValue")
            if (unit != localUnit) {
                //User switched units

                return getRemoteOneCall(lat, lon, unit)
            } else {
                //user didnt switch units and has data from before
                val localOneCallTime = localCall.current.dt.times(1000)
                val currentTime = System.currentTimeMillis()
                Timber.d("$localOneCallTime is localOneCallTime RepositoryValue")
                Timber.d("$currentTime is currentTime RepositoryValue")
                val diff = currentTime.minus(localOneCallTime)
                Timber.d("$diff is diff RepositoryValue")
                if (diff < HOUR_IN_MILLISECONDS) {
                    //Data is within the hour
                    Timber.d("Data within the hour RepositoryValue")
                    return localCall
                } else {
                    //Data is older than an hour
                    Timber.d("Data older than one hour RepositoryValue")
                    return getRemoteOneCall(lat, lon, unit)
                }
            }
        } else {
            //LocalCall is null so you get from the internet
            return getRemoteOneCall(lat, lon, unit)
        }


    }
//
//    fun getOneCallResponse(lat: String, lon: String, unit: String) = performGetOperation(
//        databaseQuery = { localDataSource.getCurrentOneCallAsLiveData() },
//        networkCall = { remoteDataSource.getOneCallLatLonResponse(lat, lon, unit) },
//        saveCallResult = { localDataSource.insertOneCall(it) }
//    )
//
//    fun getOneCallFlow(lat: String, lon: String, unit: String): Flow<OneCall> = flow {
//        emit(remoteDataSource.getOneCallLatLon(lat, lon, unit))
//    }.flowOn(Dispatchers.IO)
}