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
                val localOneCallTime = localCall.current.dt.toLong()
                val currentTime = System.currentTimeMillis()

                if (currentTime.minus(HOUR_IN_MILLISECONDS) > localOneCallTime) {
                    //Data is less than an hour old
                    return localCall
                } else {
                    //Data older than an hour so get new data
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