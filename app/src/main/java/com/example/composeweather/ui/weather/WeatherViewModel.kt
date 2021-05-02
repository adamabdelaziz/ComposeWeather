package com.example.composeweather.ui.weather

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.repository.WeatherRepository
import com.example.composeweather.util.Resource

class WeatherViewModel @ViewModelInject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    fun getCurrentOneCall(lat: Long, lon: Long): LiveData<Resource<OneCall>> {
        return repository.getCurrentOneCall(lat, lon)
    }
}




