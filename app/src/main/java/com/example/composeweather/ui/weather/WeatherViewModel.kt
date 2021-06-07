package com.example.composeweather.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweather.domain.model.Daily
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    //Get from Repository and have it as a MutableLiveData for the UI to observeAsState

    private val _oneCall = MutableLiveData<OneCall>()
    val oneCall: LiveData<OneCall> get() = _oneCall

//    private val _cards = MutableStateFlow(listOf<ExpandableCardModel>())
//    val cards: StateFlow<List<ExpandableCardModel>> get() = _cards
//
//    private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
//    val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList
//
//    private fun getDailies(dailyList: List<Daily>) {
//        viewModelScope.launch(Dispatchers.Default) {
//            val testList = arrayListOf<ExpandableCardModel>()
//            //Figure out the names of the days based on the value of dt in the Daily object
//            repeat(dailyList.size) {
//                testList += ExpandableCardModel(
//                    id = it,
//                    title = "Card $it",
//                    day = dailyList[it]
//                )
//            }
//            _cards.emit(testList)
//        }
//    }


    fun getWeather(lat: String, lon: String) {
        viewModelScope.launch {
            val weather = repository.getOneCall(lat, lon)
            _oneCall.value = weather
        }
    }

//    fun onCardArrowClicked(cardId: Int) {
//        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also { list ->
//            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
//        }
//    }
}




