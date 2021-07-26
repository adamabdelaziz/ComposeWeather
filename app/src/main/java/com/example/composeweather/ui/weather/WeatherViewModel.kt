package com.example.composeweather.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweather.domain.model.Coord
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) :
    ViewModel() {


    private val _oneCall = MutableLiveData<OneCall>()
    val oneCall: LiveData<OneCall> get() = _oneCall

    fun getWeather(lat: String, lon: String) {
        viewModelScope.launch {
            val weather = repository.getOneCall(lat, lon)
            _oneCall.value = weather
        }
    }
}




