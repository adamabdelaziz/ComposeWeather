package com.example.composeweather.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val settingsDataStore = appContext.dataStore

    //Update this whenever I think of different settings,
    //probably a different string for each theme for example

    private object PreferencesKeys {
        val CELSIUS_ENABLED = booleanPreferencesKey("celsius")
    }

    val preferencesFlow = settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d("IO Exception " + exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            //Do this for each setting, then edit the WeatherPreferences object
            val celsiusEnabled = preferences[PreferencesKeys.CELSIUS_ENABLED] ?: false
            WeatherPreferences(celsiusEnabled)
        }

    suspend fun updateCelsiusEnabled(celsiusEnabled: Boolean) {
        settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.CELSIUS_ENABLED] = celsiusEnabled

        }
    }
}


//    suspend fun setValue(key: String, value: Boolean) {
//        val wrappedKey = booleanPreferencesKey(key)
//        settingsDataStore.edit {
//            it[wrappedKey] = value
//        }
//    }
//
//    suspend fun getValue(key: String): Boolean {
//        val wrappedKey = booleanPreferencesKey(key)
//        val valueFlow: Flow<Boolean> = settingsDataStore.data.map {
//            it[wrappedKey] ?: false
//        }
//        return valueFlow.first()
//    }