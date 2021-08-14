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
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.example.composeweather.R
import com.example.composeweather.domain.model.*
import com.example.composeweather.ui.theme.ComposeWeatherTheme
import com.example.composeweather.util.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.roundToInt


@ExperimentalMaterialApi
@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()
    //private val settingsViewModel: SettingsViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var geocoder: Geocoder
    private var locationer = false

    override fun onCreate(savedInstanceState: Bundle?) {

        Timber.d("onCreate called in WeatherFragment")

        geocoder = Geocoder(context)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.onTriggerEvent(OneCallEvent.UpdateLocation(true))
                    //locationer = true
                    Timber.d("Permission granted")
                } else {
                    //Probably
                    viewModel.onTriggerEvent(OneCallEvent.UpdateLocation(false))
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
        Timber.d("onCreateViewZ called in WeatherFragment")
        refreshLocation()
        return ComposeView(requireContext()).apply {

            Timber.d("onCreateView called in WeatherFragment")

            setContent {

                val loading = viewModel.loading.value
                val prefsLoading = viewModel.prefsLoading.value
                val lat = viewModel.lat.value
                val lon = viewModel.lon.value
                val location = viewModel.location.value
                val prefs = viewModel.prefs.value
                val oneCall = viewModel.oneCall.value
                val celsiusEnabled = viewModel.celsiusEnabled.value

                if(location){
                    if (loading || prefsLoading) {
                        if(prefs!= null &&oneCall!= null){
                            ComposeWeatherTheme(prefs.lightTheme){
                                MainWeatherComponent(oneCall)
                            }
                        }else{
                            LiveDataLoadingComponent()
                        }

                    }
                    else if(!loading && !prefsLoading){
                        if(prefs != null && oneCall != null){
                            ComposeWeatherTheme(prefs.lightTheme){
                                MainWeatherComponent(oneCall)
                            }
                        }
                    }
                }
                else if(!location){
                    if(prefs != null){
                       // viewModel.onTriggerEvent(OneCallEvent.RefreshWeather(NYC_LAT, NYC_LON))
                    }

                }

            }
//            setContent {
//                val liveDataWrap = viewModel.weatherLiveDataWrap.observeAsState().value
//                val prefState = viewModel.prefs.observeAsState().value
//                val locationState = viewModel.location.observeAsState().value
//
//                if (locationState == true) {
//                    when (liveDataWrap) {
//                        is OneCallState.Empty -> {
//                            Timber.d("Empty")
//                        }
//                        is OneCallState.Failure -> {
//                            Timber.d("Failure")
//                        }
//                        is OneCallState.Loading -> {
//                            if (prefState != null) {
//                                ComposeWeatherTheme(prefState.lightTheme) {
//                                    LiveDataLoadingComponent()
//                                }
//                            }
//                        }
//                        is OneCallState.Success -> {
//                            if (prefState != null) {
//                                ComposeWeatherTheme(prefState.lightTheme) {
//                                    MainWeatherComponent(liveDataWrap.data)
//                                }
//
//                            }
//
//                        }
//
//                    }
//                } else {
//                    SetContents()
//                }
//
//            }

//            setContent {
//
//                val weatherState by oneCallLiveData.observeAsState(initial = oneCallLiveData.value)
//                val locationState by locationLiveData.observeAsState(initial = locationLiveData.value)
//                val prefState by prefLiveData.observeAsState(initial = prefLiveData.value)
//                val weatherFlow by oneCallFlow.collectAsState()
//
//                if (locationState != null) {
//                    if (locationState == true) {
//                        Timber.d("locationState isnt null and true")
//                        refreshLocation()
//                        if (weatherState != null) {
//
//                            Timber.d(prefState.toString() + " prefState")
//                            ComposeWeatherTheme(prefState!!.lightTheme) {
//                                Timber.d("path 1")
//                                MainWeatherComponent(weatherState!!)
//                            }
//                        } else {
//                            ComposeWeatherTheme(false) {
//                                Timber.d("path 2")
//                                LiveDataLoadingComponent()
//                            }
//                        }
//                    } else {
//                        Timber.d("locationState isnt null and false")
//                        viewModel.getWeather(NYC_LAT, NYC_LON)
//                        if (weatherState != null) {
//
//                            ComposeWeatherTheme(false) {
//                                Timber.d("path 3")
//                                MainWeatherComponent(weatherState!!)
//                            }
//                        } else {
//                            ComposeWeatherTheme(false) {
//                                Timber.d("path 4")
//                                LiveDataLoadingComponent()
//                            }
//                        }
//                    }
//                } else {
//                    Timber.d("locationState is null")
//                    viewModel.getWeather(NYC_LAT, NYC_LON)
//                    if (weatherState != null) {
//                        ComposeWeatherTheme(false) {
//                            Timber.d("path 5")
//                            MainWeatherComponent(weatherState!!)
//                        }
//                    } else {
//                        ComposeWeatherTheme(false) {
//                            Timber.d("path 6")
//                            LiveDataLoadingComponent()
//                        }
//                    }
//                }
//            }
//        }
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

        //val weather = remember { weatherState }
        val addressList = geocoder.getFromLocation(weatherState.lat, weatherState.lon, 5)
        val title = getTitle(addressList)


        SetStatusBar()
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,


            ) {
            TopRow(title)
            CurrentCard(weatherState)
            HourlyCard(weatherState)
            DailyCard(weatherState)
            Spacer(
                modifier = Modifier.height(16.dp)
            )

        }
    }

    private fun getTitle(addressList: List<Address>): String {
        var x = 0
        var numeric = true
        var title: String = "New York City"

        while (numeric) {
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
            title = {
                Text(
                    text = cityName,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
                )
            },
            backgroundColor = MaterialTheme.colors.primary,
            actions = {
                IconButton(onClick = {
                    refreshLocation()
                    Toast.makeText(context, "Updating..", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "Refresh Current Location",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
                IconButton(onClick = { goToSettings() }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Go to Settings Menu",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            },
        )
    }

    @Composable
    fun DailyCard(weatherState: OneCall) {
        val daily = weatherState.daily
        val offSet = weatherState.offset!!

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(daily) { day ->
                DailyRow(day, offSet)
            }
        }
    }


    //@TODO:Maybe some sort of long press somewhere that sets all expands to false?
    @Composable
    private fun DailyRow(day: Daily, offset: Double) {
        var expanded by remember { mutableStateOf(false) }

        val high = day.temp.max.roundToInt().toString()
        val low = day.temp.min.roundToInt().toString()

        val mornTemp = day.temp.morn.roundToInt().toString()
        val dayTemp = day.temp.day.roundToInt().toString()
        val eveTemp = day.temp.eve.roundToInt().toString()
        val nightTemp = day.temp.night.roundToInt().toString()

        val weather = day.weather[0]
        val icon = getIconLarge(weather.icon)

        val rain = day.rain
        val snow = day.snow
        val pop = day.pop.times(100)

        //Timber.d(icon + "   ICON LINK")

        Card(modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp).animateContentSize(),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                expanded = !expanded
                //Timber.d(expanded.toString())
            }) {
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Text(
                        text = getDayFromUnix(day.dt, offset),
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(8.dp, 4.dp),
                        //fontSize = 24.sp,

                    )
                    Image(
                        painter = rememberImagePainter(icon),
                        contentDescription = stringResource(R.string.rain_icon_description),
                        modifier = Modifier.size(64.dp).padding(8.dp, 4.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    )
                    {
                        Text(
                            style = MaterialTheme.typography.h4,
                            text = "$low$DEGREE_SYMBOL",
                            modifier = Modifier.padding(8.dp, 4.dp),


                            )
                        Text(
                            style = MaterialTheme.typography.h4,
                            text = "$high$DEGREE_SYMBOL",
                            modifier = Modifier.padding(8.dp, 4.dp),
                        )
                    }
                }
                if (expanded) {
                    //Specific Temp row. Columns used to stack the elements evenly
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = "$mornTemp$DEGREE_SYMBOL",
                                modifier = Modifier,

                                )
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = stringResource(R.string.morning),
                                modifier = Modifier,

                                )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = "$dayTemp$DEGREE_SYMBOL",
                            )
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = stringResource(R.string.day),
                            )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = "$eveTemp$DEGREE_SYMBOL",
                            )
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = stringResource(R.string.evening),
                            )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = "$nightTemp$DEGREE_SYMBOL",
                            )
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = stringResource(R.string.night),
                            )
                        }

                    }
                    //This will be the other types of things like rainfall/snowfall etc
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    )
                    {
                        if (rain > 0.0) {
                            Column(
                                modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = rememberImagePainter(RAIN_ICON_NIGHT),
                                    contentDescription = stringResource(R.string.rain_icon_description),
                                    modifier = Modifier.clickable(onClick = {
                                        Toast.makeText(
                                            context,
                                            R.string.rain_icon_description,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }).size(64.dp)
                                )
                                Text(
                                    style = MaterialTheme.typography.h5,
                                    text = "${toInches(rain)} in.",
                                    modifier = Modifier.padding(4.dp),
                                )
                            }
                        }
                        if (snow > 0.0) {
                            Column(
                                modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Image(
                                    painter = rememberImagePainter(SNOW_ICON_NIGHT),
                                    contentDescription = stringResource(R.string.snow_icon_description),
                                    modifier = Modifier.clickable(onClick = {
                                        Toast.makeText(
                                            context,
                                            R.string.snow_icon_description,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }).size(64.dp)
                                )
                                Text(
                                    style = MaterialTheme.typography.h5,
                                    text = "${toInches(snow)} in.",
                                    modifier = Modifier.padding(4.dp),

                                    )
                            }
                        }
                        if (pop > 0.0) {

                            Column(
                                modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = rememberImagePainter(R.drawable.pop),
                                    contentDescription = stringResource(R.string.pop_icon_description),
                                    modifier = Modifier.clickable(onClick = {
                                        Toast.makeText(
                                            context,
                                            R.string.pop_icon_description,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }).size(64.dp)
                                )
                                Text(
                                    style = MaterialTheme.typography.h5,
                                    text = "${pop.roundTo(2)} %",
                                    modifier = Modifier.padding(4.dp),
                                )
                            }
                        }
                    }

                }
            }
        }

    }

    @Composable
    fun Alert(alerts: List<Alert>) {
        LazyColumn() {
            items(alerts) { alert ->
                Text(
                    text = alert.event,
                    fontSize = 32.sp
                )
                Text(
                    text = alert.description,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }

    }

    /**
     * Maybe make this expandable as well? And display hourly information here? Probably wont be very useful if it shows hours that already past though
     * Also add alerts here maybe as a clickable dialog or something
     */
    @Composable
    fun CurrentCard(weatherState: OneCall) {

        val current = weatherState.current
        val alerts = weatherState.alerts
        val hourly = weatherState.hourly
        val offset = weatherState.offset

        var openDialog by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }

        val temp = current.temp.roundToInt()
        val feelsLike = current.feels_like.roundToInt()
        val humidity = current.humidity
        val clouds = current.clouds
        val windSpeed = current.wind_speed
        //val weather = current.weather.first() ?: Weather("","",0,"")

        val rainObject = current.rain ?: Rain(0.0, 0.0)
        val snowObject = current.snow ?: Snow(0.0, 0.0)

        val rain = rainObject.oneHour
        val snow = snowObject.oneHour

        Timber.d("$rain + currentCardRain")
        val timezone = weatherState.timezone


        if (openDialog) {

            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    openDialog = false
                },
                text = { Alert(alerts) },
//                    text = { Column() {
//                        for (alert in alerts) {
//                            Text(
//                                text = alert.event,
//                                fontSize = 32.sp
//                            )
//
//                            Text(
//                                text = alert.description,
//                                fontSize = 16.sp
//                            )
//                            Spacer(modifier = Modifier.size(8.dp))
//                        }
                //                   }},
                confirmButton = {
                    Button(

                        onClick = {
                            openDialog = false
                        }) {
                        Text(stringResource(R.string.thanks_bro))
                    }
                })
//                dismissButton = {
//                    Button(
//
//                        onClick = {
//                            openDialog = false
//                        }) {
//                        Text("D")
//                    }
//                })
        }

//        Card(
//            modifier = Modifier.fillMaxWidth().padding(16.dp),
//            shape = RoundedCornerShape(20.dp),
//        ) {
        Column(
            //Column modifiers go here
            modifier = Modifier.fillMaxWidth().padding(16.dp),

            ) {
            Text(
                text = "$temp$DEGREE_SYMBOL",
                modifier = Modifier.align(CenterHorizontally).padding(8.dp),
                fontSize = 64.sp,

                )
//                if (temp != feelsLike) {
//                    Text(
//                        text = "Feels like $feelsLike$DEGREE_SYMBOL",
//                        modifier = Modifier.align(CenterHorizontally).padding(8.dp),
//                        fontSize = 32.sp
//                    )
//                }
            Row(
                //Row Modifiers go here
                modifier = Modifier.align(CenterHorizontally),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically

            ) {
                if (rain > 0.0) {
                    Image(
                        painter = rememberImagePainter(RAIN_ICON_NIGHT),
                        contentDescription = stringResource(R.string.rain_icon_description),
                        modifier = Modifier.clickable(onClick = {
                            Toast.makeText(
                                context,
                                R.string.rain_icon_description,
                                Toast.LENGTH_SHORT
                            ).show()
                        }).size(64.dp)
                    )
                    Text(
                        style = MaterialTheme.typography.h5,
                        text = "${toInches(rain)} in.",
                        modifier = Modifier.padding(4.dp),
                    )
                }
                if (snow > 0.0) {
                    Image(
                        painter = rememberImagePainter(SNOW_ICON_NIGHT),
                        contentDescription = stringResource(R.string.snow_icon_description),
                        modifier = Modifier.clickable(onClick = {
                            Toast.makeText(
                                context,
                                R.string.snow_icon_description,
                                Toast.LENGTH_SHORT
                            ).show()
                        }).size(64.dp)
                    )
                    Text(
                        style = MaterialTheme.typography.h5,
                        text = "${toInches(snow)} in.",
                        modifier = Modifier.padding(4.dp),

                        )
                }
                when (clouds.toInt()) {
                    in 0..25 -> {
                        Image(
                            painter = rememberImagePainter(FEW_CLOUDS_NIGHT),
                            contentDescription = stringResource(R.string.cloud_icon_description),
                            modifier = Modifier.clickable(onClick = {
                                Toast.makeText(
                                    context,
                                    R.string.cloud_icon_description,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }).size(64.dp)
                        )
                    }
                    in 25..50 -> {
                        Image(
                            painter = rememberImagePainter(SCATTERED_CLOUDS_NIGHT),
                            contentDescription = stringResource(R.string.cloud_icon_description),
                            modifier = Modifier.clickable(onClick = {
                                Toast.makeText(
                                    context,
                                    R.string.cloud_icon_description,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }).size(64.dp)
                        )
                    }
                    in 50..100 -> {
                        Image(
                            painter = rememberImagePainter(OVERCAST_CLOUDS_NIGHT),
                            contentDescription = stringResource(R.string.cloud_icon_description),
                            modifier = Modifier.clickable(onClick = {
                                Toast.makeText(
                                    context,
                                    R.string.cloud_icon_description,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }).size(64.dp)

                        )
                    }
                }
                Text(
                    style = MaterialTheme.typography.h5,
                    text = "$clouds%",
                    modifier = Modifier.padding(8.dp),
                )
                Image(
                    painter = rememberImagePainter(R.drawable.humidity200xx),
                    contentDescription = stringResource(R.string.humidity_icon_description),
                    modifier = Modifier.clickable(onClick = {
                        Toast.makeText(
                            context,
                            R.string.humidity_icon_description,
                            Toast.LENGTH_SHORT
                        ).show()
                    }).size(64.dp)
                )
                Text(
                    style = MaterialTheme.typography.h5,
                    text = "$humidity%",
                    modifier = Modifier.padding(8.dp),
                )
                //Except this isnt always true and its crashed because it was null when there were no alerts soooo
                if (alerts !== null) {
                    Icon(
                        Icons.Rounded.Warning,
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = "warning",
                        modifier = Modifier.clickable { openDialog = true }.size(64.dp)
                            .padding(8.dp),
                    )

//                        Image(
//                            painter = rememberImagePainter(R.drawable.outline_warning_white_24),
//                            contentDescription = stringResource(R.string.humidity_icon_description),
//                            modifier = Modifier.clickable(onClick = {
//                                openDialog = true
//                            }).size(40.dp)
//                        )

                }

                //End of Row

            }
//                if (expanded) {
//                    Hourly(hourly, offset)
//                } //End of Column
        }
        //End of Card
    }


    @Composable
    fun HourlyColumn(hour: Hourly, offset: Double) {
        val dt = hour.dt

        val hourTime = getTimestampFromUnix(dt, offset)
        Timber.d(hourTime + " hourTime")

        val rain = hour.rain ?: Rain(0.0, 0.0)
        val oneHour = rain.oneHour
        val weather = hour.weather.first()
        val pop = hour.pop.times(100).roundToInt()
        val icon = getIconLarge(weather.icon)
        val temp = hour.temp.roundToInt()

        Column(
            modifier = Modifier
                .padding(start = 8.dp, top = 0.dp, bottom = 0.dp, end = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                style = MaterialTheme.typography.h5,
                text = hourTime,
                //modifier = Modifier.padding(8.dp),
            )
            if (pop > 0.0) {
                Text(
                    style = MaterialTheme.typography.subtitle2,
                    text = "$pop%",
                    //modifier = Modifier.padding(8.dp),
                )
            } else {
                Text(
                    style = MaterialTheme.typography.subtitle2,
                    text = "",
                    //modifier = Modifier.padding(8.dp),
                )
            }
            Image(
                painter = rememberImagePainter(icon),
                contentDescription = stringResource(R.string.rain_icon_description),
                modifier = Modifier.size(96.dp)
                //.padding(8.dp,4.dp)
            )
            Text(
                style = MaterialTheme.typography.h5,
                text = "$temp$DEGREE_SYMBOL",
                //modifier = Modifier.padding(8.dp),

            )
//            if(oneHour > 0.0){
//                Text(
//                    text = "$oneHour",
//                    modifier = Modifier.padding(8.dp),
//                    fontSize = 16.sp
//                )
//            }
        }
    }


    @Composable
    fun HourlyCard(weatherState: OneCall) {
        val hourly = weatherState.hourly
        val offset = weatherState.offset
//        Card(
//            modifier = Modifier.fillMaxWidth()
//                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
//                .animateContentSize(),
//            shape = RoundedCornerShape(20.dp),
//        ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 24.dp, top = 8.dp),

            ) {
            items(hourly) { hour ->
                val dt = hour.dt
                val timestamp = getTimestampFromUnix(dt, offset!!)
                val timestampDay: String = timestamp.substring(0, 3)
                val dayOfWeek: String = getDayFromUnix(dt, offset)
                Timber.d(timestamp.toString() + "dtStamp")

                HourlyColumn(hour, offset)


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
            // locationer = true
            viewModel.onTriggerEvent(OneCallEvent.UpdateLocation(true))
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
                viewModel.onTriggerEvent(
                    OneCallEvent.RefreshWeather(
                        lat.toString(),
                        lon.toString()
                    )
                )

            }
        }


    }

    private fun goToSettings() {
        val navController = findNavController()
        navController.navigate(R.id.action_weatherFragment_to_settingsFragment)
    }

}

