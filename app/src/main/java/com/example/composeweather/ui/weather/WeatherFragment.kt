package com.example.composeweather.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.ui.theme.ComposeWeatherTheme
import com.example.composeweather.util.DEGREE_SYMBOL
import com.example.composeweather.util.NYC_LAT
import com.example.composeweather.util.NYC_LON
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.roundToInt

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

                if (weatherState != null) {
                    ComposeWeatherTheme {
                        MainWeatherComponent(weatherState!!)
                    }
                } else {
                    ComposeWeatherTheme {
                        LiveDataLoadingComponent()
                    }
                }
            }
        }
    }


    @Composable
    fun MainWeatherComponent(weatherState: OneCall) {
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CurrentCard(weatherState)


        }
    }

    @Composable
    fun MinutelyCard(weatherState: OneCall) {

    }

    @Composable
    fun HourlyCard(weatherState: OneCall) {

    }

    @Composable
    fun DailyCard(weatherState: OneCall) {

    }

    @Composable
    fun CurrentCard(weatherState: OneCall) {

        val current = weatherState.current

        val temp = current.temp.roundToInt()
        val feelsLike = current.feels_like.roundToInt()
        val humidity = current.humidity
        val clouds = current.clouds
        val windSpeed = current.wind_speed

        val timezone = weatherState.timezone

        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Column(

            ) {
                Text(
                    text = "$temp$DEGREE_SYMBOL",
                    modifier = Modifier.align(CenterHorizontally).padding(8.dp),
                    fontSize = 64.sp,

                    )
                if(temp != feelsLike){
                    Text(
                        text = "Feels like $feelsLike$DEGREE_SYMBOL",
                        modifier = Modifier.align(CenterHorizontally).padding(8.dp),
                        fontSize = 24.sp
                    )
                }

            }
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)

    }


    @Preview
    @Composable
    fun PreviewScreen() {

        viewModel.getWeather(NYC_LAT, NYC_LON)
        val weatherState = viewModel.oneCall.observeAsState().value!!

        MainWeatherComponent(weatherState)
    }
}
