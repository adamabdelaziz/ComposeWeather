package com.adamcodem.composeweather.repository

import com.adamcodem.composeweather.domain.dao.WeatherDao
import com.adamcodem.composeweather.domain.model.OneCall
import com.adamcodem.composeweather.network.WeatherRemoteDataSource
import com.adamcodem.composeweather.util.HOUR_IN_MILLISECONDS
import com.adamcodem.composeweather.util.NYC_LAT
import com.adamcodem.composeweather.util.NYC_LON
import timber.log.Timber
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherDao,
) {
    suspend fun insertOneCall(oneCall: OneCall) {
        Timber.d("insertOneCall() called RepositoryValue")
        Timber.d("${oneCall.id} +  oneCall ID insertOneCall() RepositoryValue")
        localDataSource.insertOneCall(oneCall)
    }


    suspend fun getRemoteOneCall(lat: String, lon: String, unit: String): OneCall {
        Timber.d("getRemoteOneCall() called RepositoryValue")
        val oneCall = remoteDataSource.getOneCallLatLon(lat, lon, unit)
        if (oneCall != null) {
            Timber.d("${oneCall.id} +  oneCall ID getRemoteOneCall() RepositoryValue")
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
        Timber.d("getLocalOneCall() called RepositoryValue")
        val oneCall = localDataSource.getCurrentOneCall()
        if (oneCall == null) {
            Timber.d("getLocalOneCall() oneCall is null and I got fucking trolled RepositoryValue")
        } else {
            Timber.d("${oneCall.id} +  oneCall ID getLocalOneCall() RepositoryValue")
        }

        return oneCall


    }

    suspend fun getCorrectOneCall(lat: String, lon: String, unit: String): OneCall {
        Timber.d("getCorrectOneCall() called RepositoryValue")
        val localCall = getLocalOneCall()

        Timber.d(unit + " unit RepositoryValue")

        if (localCall != null) {
            Timber.d("${localCall.id} +  oneCall ID getCorrectOneCall() RepositoryValue")
            val localUnit = localCall.unit
            Timber.d(localUnit + " localUnit RepositoryValue")
            if (unit != localUnit || localCall.lat.toString() == NYC_LAT || localCall.lat.toString() == NYC_LON) {
                //User switched units or just granted permissions
                Timber.d(localCall.lat.toString() + "localCallLat RepositoryValue")
                Timber.d(localCall.lon.toString() + "localCallLon RepositoryValue")
                Timber.d("New units or location just enabled RepositoryValue")
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
            Timber.d("getCorrectOneCall() oneCall is null and calling getRemoteOneCall() RepositoryValue")
            return getRemoteOneCall(lat, lon, unit)
        }


    }

}