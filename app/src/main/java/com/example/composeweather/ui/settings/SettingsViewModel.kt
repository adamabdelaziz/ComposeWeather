package com.example.composeweather.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweather.preference.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val dataSoreManager: DataStoreManager) :
    ViewModel() {

    //Gets settings as a Flow

    val preferencesFlow = dataSoreManager.preferencesFlow

    //Fragment interacts with this method which interacts with DataStoreManager

    fun onCelsiusSettingSelected(celsiusSetting : Boolean){
        viewModelScope.launch {
            dataSoreManager.updateCelsiusEnabled(celsiusSetting)
        }
    }

}


//private val _setting = MutableLiveData<Boolean>()
//val setting: LiveData<Boolean> get() = _setting
//
//fun getSettingValue(key: String) {
//    viewModelScope.launch {
//        val settingResult = dataSoreManager.getValue(key)
//        _setting.value = settingResult
//    }
//}