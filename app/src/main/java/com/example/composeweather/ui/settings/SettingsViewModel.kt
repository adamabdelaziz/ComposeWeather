package com.example.composeweather.ui.settings

import androidx.lifecycle.*
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.preference.DataStoreManager
import com.example.composeweather.preference.WeatherPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataSoreManager: DataStoreManager) :
    ViewModel() {

    //Gets settings as a Flow

    private val preferencesFlow = dataSoreManager.preferencesFlow
    private val locationFlow = dataSoreManager.locationFlow

    private val _prefs = MutableLiveData<WeatherPreferences>()
    val prefs : LiveData<WeatherPreferences> get() = _prefs

    private val _location = MutableLiveData<Boolean>()
    val location: LiveData<Boolean> get() = _location

    //Fragment interacts with this method which interacts with DataStoreManager

    fun onCelsiusSettingSelected(celsiusSetting : Boolean){
        viewModelScope.launch {
            dataSoreManager.updateCelsiusEnabled(celsiusSetting)
            _prefs.value = preferencesFlow.first()
        }
    }
    fun onLightThemeSettingSelected(lightThemeSetting : Boolean){
        viewModelScope.launch {
            dataSoreManager.updateLightThemeEnabled(lightThemeSetting)
            _prefs.value = preferencesFlow.first()
        }
    }

    fun onLocationSettingsSelected(locationSetting: Boolean){
        viewModelScope.launch {
            dataSoreManager.updateLocationEnabled(locationSetting)
            _location.value = locationSetting
        }
    }


    init{
        Timber.d("SettingsViewModel init start")
        viewModelScope.launch {
            _location.value = locationFlow.first()
            _prefs.value = preferencesFlow.first()
        }
        Timber.d("SettingsViewModel init end")
    }
}
