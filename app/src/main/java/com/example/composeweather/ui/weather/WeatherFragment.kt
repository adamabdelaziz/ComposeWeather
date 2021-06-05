package com.example.composeweather.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.composeweather.R
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.domain.model.Rain
import com.example.composeweather.domain.model.Snow
import com.example.composeweather.ui.theme.ComposeWeatherTheme
import com.example.composeweather.util.*
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {

        geocoder = Geocoder(context)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Timber.d("Permission granted")
                    //Wondering if this will call it twice in a row but if the permission gets granted for the first time this should allow it to continue to update the weather
                    //instead of making the user click the button twice
                    refreshLocation()
                } else {
                    Timber.d("Permission denied")
                    Toast.makeText(
                        context,
                        "The app will not work without location services",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {


            //Trying to get rid of this fake data but not quite there yet
            viewModel.getWeather(NYC_LAT, NYC_LON)
            Timber.d("getWeather Called in VM from Fragment with that boofy data")

            val oneCallLiveData = viewModel.oneCall

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Timber.d("Permission already granted from before onCreateView() is called")
                refreshLocation()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }


            setContent {
                Timber.d("setContent called")
                val weatherState by oneCallLiveData.observeAsState(initial = oneCallLiveData.value)

                if (weatherState != null) {
                    ComposeWeatherTheme {
                        Timber.d("weatherState!=null; calling MainWeatherComponent()")
                        MainWeatherComponent(weatherState!!)
                    }
                } else {
                    ComposeWeatherTheme {
                        Timber.d("weatherState==null; calling LiveDataLoadingComponent()")
                        LiveDataLoadingComponent()
                    }
                }
            }
        }
    }

    @Composable
    fun SetStatusBar() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = MaterialTheme.colors.isLight
        val color = MaterialTheme.colors.primary
        SideEffect {
            // Update all of the system bar colors to be transparent, and use
            // dark icons if we're in light theme
            systemUiController.setSystemBarsColor(
                color = color,
                darkIcons = useDarkIcons
            )
        }
    }

    @Composable
    fun MainWeatherComponent(weatherState: OneCall) {


        val addressList = geocoder.getFromLocation(weatherState.lat, weatherState.lon, 5)
        val title = getTitle(addressList)

//        val title: String = if (addressList.elementAt(2).featureName != null) {
//            addressList.elementAt(2).featureName
//        } else {
//            "New York City"
//        }

        SetStatusBar()
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopRow(title)
            CurrentCard(weatherState)


        }
    }

    private fun getTitle(addressList: List<Address>): String {
        var x = 0
        var numeric = true
        var title : String = "New York City"

        while(numeric){
            title = addressList[x].featureName
            x++
            numeric = title.matches(".*\\d.*".toRegex())
            Timber.d(numeric.toString() + "" + title)
        }

        return title
    }

    @Composable
    fun TopRow(cityName: String) {
        TopAppBar(
            title = { Text(cityName) },
            backgroundColor = MaterialTheme.colors.primary,
            actions = {
                IconButton(onClick = { refreshLocation() }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Refresh Current Location")
                }
                IconButton(onClick = { goToSettings() }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Go to Settings Menu")
                }
            },

            )
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

        val rainObject = current.rain ?: Rain(0.0, 0.0)
        val snowObject = current.snow ?: Snow(0.0, 0.0)

        val rain = rainObject.oneHour
        val snow = snowObject.oneHour

        Timber.d("$rain + currentCardRain")
        val timezone = weatherState.timezone

        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Column(
                //Column modifiers go here

            ) {
                Text(
                    text = "$temp$DEGREE_SYMBOL",
                    modifier = Modifier.align(CenterHorizontally).padding(8.dp),
                    fontSize = 64.sp,

                    )
                if (temp != feelsLike) {
                    Text(
                        text = "Feels like $feelsLike$DEGREE_SYMBOL",
                        modifier = Modifier.align(CenterHorizontally).padding(8.dp),
                        fontSize = 24.sp
                    )
                }
                Row(
                    //Row Modifiers go here
                    modifier = Modifier.align(CenterHorizontally),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    if (rain > 0.0) {
                        Image(
                            painter = rememberCoilPainter(RAIN_ICON_NIGHT),
                            contentDescription = stringResource(R.string.rain_icon_description),
                            modifier = Modifier.clickable(onClick = {
                                Toast.makeText(
                                    context,
                                    R.string.rain_icon_description,
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        )
                        Text(
                            text = "${toInches(rain)} in.",
                            modifier = Modifier.padding(4.dp),
                            fontSize = 16.sp
                        )
                    }
                    if (snow > 0.0) {
                        Image(
                            painter = rememberCoilPainter(SNOW_ICON_NIGHT),
                            contentDescription = stringResource(R.string.snow_icon_description),
                            modifier = Modifier.clickable(onClick = {
                                Toast.makeText(
                                    context,
                                    R.string.snow_icon_description,
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        )
                        Text(
                            text = "${toInches(snow)} in.",
                            modifier = Modifier.padding(4.dp),
                            fontSize = 16.sp
                        )
                    }
                    when (clouds.toInt()) {
                        in 0..25 -> {
                            Image(
                                painter = rememberCoilPainter(FEW_CLOUDS_NIGHT),
                                contentDescription = stringResource(R.string.cloud_icon_description),
                                modifier = Modifier.clickable(onClick = {
                                    Toast.makeText(
                                        context,
                                        R.string.cloud_icon_description,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                            )
                        }
                        in 25..50 -> {
                            Image(
                                painter = rememberCoilPainter(SCATTERED_CLOUDS_NIGHT),
                                contentDescription = stringResource(R.string.cloud_icon_description),
                                modifier = Modifier.clickable(onClick = {
                                    Toast.makeText(
                                        context,
                                        R.string.cloud_icon_description,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                            )
                        }
                        in 50..100 -> {
                            Image(
                                painter = rememberCoilPainter(OVERCAST_CLOUDS_NIGHT),
                                contentDescription = stringResource(R.string.cloud_icon_description),
                                modifier = Modifier.clickable(onClick = {
                                    Toast.makeText(
                                        context,
                                        R.string.cloud_icon_description,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })

                            )
                        }
                    }
                    Text(
                        text = "$clouds%",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp
                    )
                    Image(
                        painter = painterResource(R.drawable.humidity200xx),
                        contentDescription = stringResource(R.string.humidity_icon_description),
                        modifier = Modifier.clickable(onClick = {
                            Toast.makeText(
                                context,
                                R.string.humidity_icon_description,
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    )
                    Text(
                        text = "$humidity%",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 16.sp
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


    private fun refreshLocation() {

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.d("Do not have location, requesting permission.")
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            Timber.d("Have permission, refreshing location")

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                val lat: Double
                val lon: Double

                if (location == null) {
                    lat = NYC_LAT.toDouble()
                    lon = NYC_LON.toDouble()
                } else {
                    lat = location.latitude
                    lon = location.longitude
                }


                val addressList = geocoder.getFromLocation(lat, lon, 10)
                for (address in addressList) {
                    Timber.d(addressList.indexOf(address).toString() + " index")
                    Timber.d(address.adminArea + " adminArea")
                    Timber.d(address.countryCode + " countryCode")
                    Timber.d(address.countryName + " countryName")
                    Timber.d(address.featureName + " featureName")
                    Timber.d(address.locality + " locality")
                    Timber.d(address.phone + " phone]")
                    Timber.d(address.premises + " premises")

                }
                viewModel.getWeather(lat.toString(), lon.toString())
            }
        }


    }

    private fun goToSettings() {

    }

}
