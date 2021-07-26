package com.example.composeweather.di

import android.content.Context
import com.example.composeweather.domain.dao.WeatherDao
import com.example.composeweather.domain.db.AppDatabase
import com.example.composeweather.network.WeatherService
import com.example.composeweather.preference.DataStoreManager
import com.example.composeweather.repository.WeatherRepository
import com.example.composeweather.util.BASE_URL
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
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
    fun provideFusedLocationProvider(@ApplicationContext appContext: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: WeatherService, localDataSource: WeatherDao) =
        WeatherRepository(remoteDataSource, localDataSource)


}