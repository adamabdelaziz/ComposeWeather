package com.example.composeweather.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweather.domain.model.Coord
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.preference.DataStoreManager
import com.example.composeweather.preference.WeatherPreferences
import com.example.composeweather.repository.WeatherRepository
import com.example.composeweather.util.CELSIUS
import com.example.composeweather.util.FAHRENHEIT
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val dataSoreManager: DataStoreManager
) :
    ViewModel() {

    private val preferencesFlow = dataSoreManager.preferencesFlow


    private val _oneCall = MutableLiveData<OneCall>()
    val oneCall: LiveData<OneCall> get() = _oneCall

    fun getWeather(lat: String, lon: String) {
        viewModelScope.launch {
            val weatherPreferences = preferencesFlow.first()


            if(weatherPreferences.celsiusEnabled){
                val weather = repository.getOneCall(lat, lon, CELSIUS)
                _oneCall.value = weather
            }else{
                val weather = repository.getOneCall(lat, lon, FAHRENHEIT)
                _oneCall.value = weather
            }

        }
    }
}




