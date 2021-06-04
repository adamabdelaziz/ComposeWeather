package com.example.composeweather.ui.weather

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweather.domain.db.AppDatabase
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    //Get from Repository and have it as a MutableLiveData for the UI to observeAsState

    private val _oneCall = MutableLiveData<OneCall>()

    val oneCall: LiveData<OneCall>
        get() = _oneCall

    fun getWeather(lat: String, lon: String) {
        viewModelScope.launch {
            val weather = repository.getOneCall(lat, lon)
            _oneCall.value = weather
        }
    }
}




