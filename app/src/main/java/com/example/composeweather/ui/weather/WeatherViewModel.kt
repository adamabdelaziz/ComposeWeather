package com.example.composeweather.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.preference.DataStoreManager
import com.example.composeweather.preference.WeatherPreferences
import com.example.composeweather.repository.WeatherRepository
import com.example.composeweather.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val dataSoreManager: DataStoreManager
) :
    ViewModel() {

    private val preferencesFlow = dataSoreManager.preferencesFlow
    private val locationFlow = dataSoreManager.locationFlow

    private val _oneCall = MutableLiveData<OneCall>()
    val oneCall: LiveData<OneCall> get() = _oneCall

    private val _prefs = MutableLiveData<WeatherPreferences>()
    val prefs: LiveData<WeatherPreferences> get() = _prefs

    private val _location = MutableLiveData<Boolean>()
    val location: LiveData<Boolean> get() = _location

    val oneCallLiveData: LiveData<Resource<OneCall>> = repository.getOneCallResponse(
        NYC_LAT,
        NYC_LON, FAHRENHEIT
    )

    private val _oneCallStateFlow: MutableStateFlow<OneCallState> =
        MutableStateFlow(OneCallState.Empty)
    val oneCallStateFlow: StateFlow<OneCallState> = _oneCallStateFlow

    fun getOneCallFlow(lat: String, lon: String) = viewModelScope.launch {

            _oneCallStateFlow.value = OneCallState.Loading


        val weatherPreferences = preferencesFlow.first()

        if (weatherPreferences.celsiusEnabled) {
            repository.getOneCallFlow(lat, lon, CELSIUS)
                .catch { e -> _oneCallStateFlow.value = OneCallState.Failure(e) }
                .collect { data -> _oneCallStateFlow.value = OneCallState.Success(data) }

        } else {
            repository.getOneCallFlow(lat, lon, FAHRENHEIT)
                .catch { e -> _oneCallStateFlow.value = OneCallState.Failure(e) }
                .collect { data -> _oneCallStateFlow.value = OneCallState.Success(data) }

        }
    }

    fun getWeather(lat: String, lon: String) {
        viewModelScope.launch {
            val weatherPreferences = preferencesFlow.first()

            if (weatherPreferences.celsiusEnabled) {
                val weather = repository.getOneCall(lat, lon, CELSIUS)
                _oneCall.value = weather
            } else {
                val weather = repository.getOneCall(lat, lon, FAHRENHEIT)
                _oneCall.value = weather
            }

        }
    }

    fun onLocationSettingsSelected(locationSetting: Boolean) {
        viewModelScope.launch {
            dataSoreManager.updateLocationEnabled(locationSetting)
            _location.value = locationSetting
        }
    }

    fun getPrefs() {
        viewModelScope.launch {
            _location.value = locationFlow.first()
            _prefs.value = preferencesFlow.first()
        }
    }

    init {
        Timber.d("WeatherViewModel init start")
        getPrefs()
        Timber.d("WeatherViewModel init end")
    }


}




