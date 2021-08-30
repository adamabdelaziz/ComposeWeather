package com.example.composeweather.ui.weather


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.Assisted
import androidx.lifecycle.*
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.preference.DataStoreManager
import com.example.composeweather.preference.WeatherPreferences
import com.example.composeweather.repository.WeatherRepository
import com.example.composeweather.util.*
import com.example.composeweather.util.OneCallEvent.*
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
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val preferencesFlow = dataSoreManager.preferencesFlow
    private val locationFlow = dataSoreManager.locationFlow

//    private val _oneCall = MutableLiveData<OneCall>()
//    val oneCall: LiveData<OneCall> get() = _oneCall

//    private val _prefs = MutableLiveData<WeatherPreferences>()
//    val prefs: LiveData<WeatherPreferences> get() = _prefs

//    private val _location = MutableLiveData<Boolean>()
//    val location: LiveData<Boolean> get() = _location


//    private val _oneCallStateFlow: MutableStateFlow<OneCallState> =
//        MutableStateFlow(OneCallState.Empty)
//    val oneCallStateFlow: StateFlow<OneCallState> = _oneCallStateFlow


    val loading = mutableStateOf(false)
    val prefsLoading = mutableStateOf(false)
    val lat = mutableStateOf(NYC_LAT)
    val lon = mutableStateOf(NYC_LON)
    val location = mutableStateOf(false)

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
        }
        else{
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
        if (celsiusEnabled){
            oneCall.value = repository.getCorrectOneCall(lat.value, lon.value, CELSIUS)
            loading.value = false
        }else
        {
            oneCall.value = repository.getCorrectOneCall(lat.value, lon.value, FAHRENHEIT)
            loading.value = false
        }


    }

    private suspend fun getWeather() {
        loading.value = true
//        val cringeCall= OneCall(
//            Current(
//                0.0,
//                0.0, 0.0, 0.0, 0.0,
//                0.0, 0.0, 0.0, 0.0, 0.0,
//                0.0, listOf(), 0.0, 0.0, 0.0, Rain(0.0, 0.0), Snow(0.0, 0.0)
//            ), listOf(), listOf(), lat.value.toDouble(), lon.value.toDouble(), listOf(), listOf(), "", 0.0
//        )
//        oneCall.value =cringeCall
//        repository.insertOneCall(cringeCall)
        //val celsiusEnabled = prefs.value!!.celsiusEnabled

        val weatherPreferences = preferencesFlow.first()
        if (celsiusEnabled.value) {
            oneCall.value = repository.getCorrectOneCall(lat.value, lon.value, CELSIUS)
            loading.value = false
        } else {
            oneCall.value = repository.getCorrectOneCall(lat.value, lon.value, FAHRENHEIT)
            loading.value = false
        }
        //loading.value =false
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
//    fun getWeather(lat: String, lon: String) {
//        viewModelScope.launch {
//            val weatherPreferences = preferencesFlow.first()
//
//            if (weatherPreferences.celsiusEnabled) {
//                val weather = repository.getOneCall(lat, lon, CELSIUS)
//                _oneCall.value = weather
//            } else {
//                val weather = repository.getOneCall(lat, lon, FAHRENHEIT)
//                _oneCall.value = weather
//            }
//
//        }
//    }
//
//    fun onLocationSettingsSelected(locationSetting: Boolean) {
//        viewModelScope.launch {
//            dataSoreManager.updateLocationEnabled(locationSetting)
//            _location.value = locationSetting
//        }
//    }
//


    // private val _weatherLiveDataWrap = MutableLiveData<OneCallState>()

//    val weatherLiveDataWrap: LiveData<OneCallState> get() = _weatherLiveDataWrap
//
//    fun getOneCallLiveDataWrap(lat: String, lon: String) {
//        viewModelScope.launch {
//            val weatherPreferences = preferencesFlow.first()
//            if (_weatherLiveDataWrap.value == OneCallState.Empty) {
//                _weatherLiveDataWrap.value = OneCallState.Loading
//            }
//            if(weatherPreferences.celsiusEnabled){
//                val weather = repository.getOneCall(lat,lon, CELSIUS)
//                _weatherLiveDataWrap.value = OneCallState.Success(weather)
//            }else{
//                val weather = repository.getOneCall(lat,lon, FAHRENHEIT)
//                _weatherLiveDataWrap.value = OneCallState.Success(weather)
//            }
//
//        }
//    }
//
//    fun getOneCallFlow(lat: String, lon: String) {
//        viewModelScope.launch {
//            _oneCallStateFlow.value = OneCallState.Loading
//            val weatherPreferences = preferencesFlow.first()
//
//            if (weatherPreferences.celsiusEnabled) {
//                repository.getOneCallFlow(lat, lon, CELSIUS)
//                    .catch { e -> _oneCallStateFlow.value = OneCallState.Failure(e) }
//                    .collect { data -> _oneCallStateFlow.value = OneCallState.Success(data) }
//
//            } else {
//                repository.getOneCallFlow(lat, lon, FAHRENHEIT)
//                    .catch { e -> _oneCallStateFlow.value = OneCallState.Failure(e) }
//                    .collect { data -> _oneCallStateFlow.value = OneCallState.Success(data) }
//
//            }
//        }
//    }
}




