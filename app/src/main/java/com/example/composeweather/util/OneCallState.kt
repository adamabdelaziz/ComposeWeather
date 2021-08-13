package com.example.composeweather.util

import com.example.composeweather.domain.model.OneCall

sealed class OneCallState {
    object Loading : OneCallState()
    class Failure(val message: Throwable) : OneCallState()
    class Success(val data: OneCall) : OneCallState()
    object Empty : OneCallState()
}
