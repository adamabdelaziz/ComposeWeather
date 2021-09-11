package com.adamcodem.composeweather.util


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.adamcodem.composeweather.domain.model.Coord
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class LocationHelper @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder
) {
    init {
        hasPermission()
    }

    fun hasPermission(): Boolean {
        Timber.d("hasPermission() called")
        val permissionGranted = ContextCompat.checkSelfPermission(
            fusedLocationProviderClient.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (permissionGranted) {

        }

        return permissionGranted
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): Coord {
        Timber.d("getLocation() called")
        return fusedLocationProviderClient.lastLocation.await().run {
            Coord(this.latitude, this.longitude)
        }


    }

    fun getTitle(lat: Double, lon: Double) : String {
        Timber.d("getTitle() called")
        val addressList = geocoder.getFromLocation(lat, lon, 10)

        var x = 0
        var numeric = true
        var title: String = "New York City"

        while (numeric) {
            title = addressList[x].featureName
            x++
            numeric = title.matches(".*\\d.*".toRegex())
            Timber.d(numeric.toString() + "" + title)
        }

        return title
    }
}