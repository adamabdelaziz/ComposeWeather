package com.example.composeweather.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.util.NYC_LAT
import com.example.composeweather.util.NYC_LON
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            //Use default Lat and Lon so some value is displayed
            viewModel.getWeather(NYC_LAT, NYC_LON)
            val oneCallLiveData = viewModel.oneCall
            Timber.d("getWeather Called in VM from Fragment")

            setContent {

                val weatherState by oneCallLiveData.observeAsState(initial = oneCallLiveData.value)

                if(weatherState!= null){
                    MainWeatherComponent(weatherState!!)
                }
                else{
                    LiveDataLoadingComponent()
                }

//                Column(modifier = Modifier.fillMaxSize()) {
//                    Text(text = "This is Weather Fragment")
//                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)

    }
    @Composable
    fun MainWeatherComponent(weatherState : OneCall){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weatherState.timezone

            )



        }
    }
    @Composable
    fun LiveDataLoadingComponent() {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
        }
    }
}
