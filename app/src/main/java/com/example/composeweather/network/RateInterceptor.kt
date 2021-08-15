package com.example.composeweather.network

import android.os.SystemClock
import okhttp3.Interceptor
import okhttp3.Response

class RateInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        SystemClock.sleep(1000)
        return chain.proceed(request)

    }
}