package com.adamcodem.composeweather.di

import android.content.Context
import android.location.Geocoder

import com.adamcodem.composeweather.domain.dao.WeatherDao
import com.adamcodem.composeweather.domain.db.AppDatabase
import com.adamcodem.composeweather.network.RateInterceptor
import com.adamcodem.composeweather.network.WeatherRemoteDataSource
import com.adamcodem.composeweather.network.WeatherService
import com.adamcodem.composeweather.preference.DataStoreManager
import com.adamcodem.composeweather.repository.WeatherRepository
import com.adamcodem.composeweather.util.BASE_URL
import com.adamcodem.composeweather.util.LocationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideNetworkInterceptor(): RateInterceptor = RateInterceptor()

    @Singleton
    @Provides
    fun provideOkHttpClient(rateInterceptor: RateInterceptor): OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(rateInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun providesGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(
        appContext
    )

    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext appContext: Context): DataStoreManager =
        DataStoreManager(appContext)

    @Singleton
    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao()


    @Provides
    @Singleton
    fun provideFusedLocationProvider(@ApplicationContext appContext: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)
    @Provides
    @Singleton
    fun providesGeocoder(@ApplicationContext appContext: Context): Geocoder = Geocoder(appContext)

    @Provides
    @Singleton
    fun providesLocationHelper(fusedLocationProviderClient: FusedLocationProviderClient, geocoder: Geocoder) : LocationHelper = LocationHelper(fusedLocationProviderClient,geocoder)
//    @Provides
//    @Singleton
//    fun provideLocationManager(fusedLocationProviderClient: FusedLocationProviderClient): LocationManager1 {
//        val locationManager = LocationManager1(fusedLocationProviderClient)
//        return locationManager
//    }

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: WeatherRemoteDataSource, localDataSource: WeatherDao) =
        WeatherRepository(remoteDataSource, localDataSource)


}