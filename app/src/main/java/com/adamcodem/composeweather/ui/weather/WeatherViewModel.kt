package com.adamcodem.composeweather.ui.weather


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamcodem.composeweather.domain.model.OneCall
import com.adamcodem.composeweather.preference.DataStoreManager
import com.adamcodem.composeweather.preference.WeatherPreferences
import com.adamcodem.composeweather.repository.WeatherRepository
import com.adamcodem.composeweather.util.*
import com.adamcodem.composeweather.util.OneCallEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val USER_LAT = "user_lat"
const val USER_LON = "user_lon"
const val USER_LOCATION = "user_location"

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val dataSoreManager: DataStoreManager,
    private val LocationHelper: LocationHelper,
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val preferencesFlow = dataSoreManager.preferencesFlow
    private val locationFlow = dataSoreManager.locationFlow

    val loading = mutableStateOf(false)
    val prefsLoading = mutableStateOf(false)
    val lat = mutableStateOf(NYC_LAT)
    val lon = mutableStateOf(NYC_LON)
    val location = mutableStateOf(false)
    val title = mutableStateOf("")
    val prefs: MutableState<WeatherPreferences?> = mutableStateOf(null)

    val lightTheme = mutableStateOf(false)
    val celsiusEnabled = mutableStateOf(false)

    val oneCall: MutableState<OneCall?> = mutableStateOf(null)

    init {
        Timber.d("WeatherViewModel init start")
        getPrefs()
        savedStateHandle.get<String>(USER_LAT)?.let { lat ->
            Timber.d("restoring $lat")
            setLat(lat)
        }
        savedStateHandle.get<String>(USER_LON)?.let { lon ->
            Timber.d("restoring $lon")
            setLon(lon)
        }
        savedStateHandle.get<Boolean>(USER_LON)?.let { location ->
            Timber.d("restoring $location")
            setLocationSetting(location)
        }
        if (lat.value != NYC_LAT && lon.value != NYC_LON) {
            onTriggerEvent(RestoreStateEvent)
        } else {
            onTriggerEvent(RefreshWeather(NYC_LAT, NYC_LON))
        }
        Timber.d("WeatherViewModel init end")
    }

    fun onTriggerEvent(event: OneCallEvent) {
        viewModelScope.launch {
            when (event) {
                is RefreshWeather -> {
                    Timber.d("${event.lat} RefreshWeather lat value")
                    Timber.d("${event.lon} RefreshWeather lat value")
                    lat.value = event.lat
                    lon.value = event.lon
                    Timber.d("onTriggerEvent RefreshWeather")
                    getPrefs()
                    getWeather()

                }
                is RestoreStateEvent -> {
                    Timber.d("onTriggerEvent RestoreStateEvent")
                    getPrefs()
                    restoreScreen()
                }
                is UpdateLocation -> {
                    val boolean = event.locationSetting
                    Timber.d("onTriggerEvent UpdateLocation")
                    getPrefs()
                    updateDataStoreLocation(boolean)
                }
            }
        }
    }

    private suspend fun restoreScreen() {
        loading.value = true
        val celsiusEnabled = preferencesFlow.first().celsiusEnabled
        if (celsiusEnabled) {
            oneCall.value = repository.getCorrectOneCall(lat.value, lon.value, CELSIUS)
            loading.value = false
        } else {
            oneCall.value = repository.getCorrectOneCall(lat.value, lon.value, FAHRENHEIT)
            loading.value = false
        }


    }

    private suspend fun getWeather() {
        loading.value = true
        if (celsiusEnabled.value) {
            oneCall.value = repository.getCorrectOneCall(lat.value, lon.value, CELSIUS)
            getTitle()
            loading.value = false
        } else {
            oneCall.value = repository.getCorrectOneCall(lat.value, lon.value, FAHRENHEIT)
            getTitle()
            loading.value = false
        }

    }

    private suspend fun updateDataStoreLocation(locationSetting: Boolean) {
        dataSoreManager.updateLocationEnabled(locationSetting)
        setLocationSetting(locationSetting)
    }

    fun setLat(lat: String) {
        this.lat.value = lat
        savedStateHandle.set(USER_LAT, lat)
    }

    fun setLon(lon: String) {
        this.lon.value = lon
        savedStateHandle.set(USER_LON, lon)
    }

    fun setLocationSetting(locationSetting: Boolean) {
        this.location.value = locationSetting
        savedStateHandle.set(USER_LOCATION, locationSetting)
    }

    fun refreshLocation() {
        viewModelScope.launch {
            val coord = LocationHelper.getLocation()

            onTriggerEvent(RefreshWeather(coord.lat.toString(), coord.lon.toString()))
        }

    }
    fun getTitle(){
        title.value = LocationHelper.getTitle(oneCall.value!!.lat,oneCall.value!!.lon)
    }
    private fun getPrefs() {
        viewModelScope.launch {
            Timber.d("getPrefs() started")
            prefsLoading.value = true
            location.value = locationFlow.first()
            prefs.value = preferencesFlow.first()
            lightTheme.value = prefs.value!!.lightTheme
            celsiusEnabled.value = prefs.value!!.celsiusEnabled
            prefsLoading.value = false
            Timber.d("getPrefs() done")
        }
    }

}




