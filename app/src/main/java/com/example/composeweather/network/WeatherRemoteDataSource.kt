package com.example.composeweather.network

import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(private val weatherService: WeatherService) :
    BaseDataSource() {


}