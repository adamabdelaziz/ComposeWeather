package com.example.composeweather.ui.weather

//import android.location.Address
//import android.location.Geocoder
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.compose.animation.animateContentSize
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.runtime.*
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Alignment.Companion.CenterHorizontally
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.rememberImagePainter
//import com.example.composeweather.R
//import com.example.composeweather.domain.model.Daily
//import com.example.composeweather.domain.model.OneCall
//import com.example.composeweather.domain.model.Rain
//import com.example.composeweather.domain.model.Snow
//import com.example.composeweather.ui.theme.ComposeWeatherTheme
//import com.example.composeweather.util.*
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.PermissionRequired
//import com.google.accompanist.permissions.rememberPermissionState
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
//import com.google.android.gms.location.FusedLocationProviderClient
//import timber.log.Timber
//import kotlin.math.roundToInt
//
//
//private fun refreshLocation(weatherViewModel: WeatherViewModel, fusedLocationProviderClient: FusedLocationProviderClient) {
//    weatherViewModel.getCoord()
//    val coord = weatherViewModel.coord.value!!
//    Timber.d("Refresh Location Called")
//    Timber.d(coord.lat.toString())
//    Timber.d(coord.lon.toString())
//    weatherViewModel.getWeather(coord.lat.toString(), coord.lon.toString())
//}
//
//
//@ExperimentalPermissionsApi
//@ExperimentalMaterialApi
//@Composable
//fun WeatherScreen(
//    navController: NavController,
//    weatherViewModel: WeatherViewModel,
//    fusedLocationProviderClient: FusedLocationProviderClient
//) {
//
//
//
//
//    val context = LocalContext.current
//    val geocoder = Geocoder(context)
//
//    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }
//    val locationPermissionState =
//        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
//
//    PermissionRequired(
//        permissionState = locationPermissionState,
//        permissionNotGrantedContent = {
//            if (doNotShowRationale) {
//                Text("Feature not available")
//            } else {
//                Column {
//                    Text("The camera is important for this app. Please grant the permission.")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Row {
//                        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
//                            Text("Ok!")
//                        }
//                        Spacer(Modifier.width(8.dp))
//                        Button(onClick = { doNotShowRationale = true }) {
//                            Text("Nope")
//                        }
//                    }
//                }
//            }
//        },
//        permissionNotAvailableContent = {
//            Column {
//                Text(
//                    "Camera permission denied. See this FAQ with information about why we " +
//                        "need this permission. Please, grant us access on the Settings screen."
//                )
//
//            }
//        }
//    ) {
//        Text("Camera permission Granted")
//    }
//
//    //This must have permissions before being called or you get xD
//    //refreshLocation(weatherViewModel)
//    weatherViewModel.getCoord()
//    val coordinate = weatherViewModel.coord
//    val coordinates by  coordinate.observeAsState(initial = coordinate.value)
//
//    weatherViewModel.getWeather(coordinates!!.lat.toString(), coordinates!!.lon.toString())
//    val oneCallLiveData = weatherViewModel.oneCall
//    val weatherState by oneCallLiveData.observeAsState(initial = oneCallLiveData.value)
//
//    if (weatherState != null) {
//        ComposeWeatherTheme {
//            Timber.d("weatherState!=null; calling MainWeatherComponent()")
//            MainWeatherComponent(weatherState!!,geocoder,weatherViewModel,fusedLocationProviderClient)
//        }
//    } else {
//        ComposeWeatherTheme {
//            Timber.d("weatherState==null; calling LiveDataLoadingComponent()")
//            LiveDataLoadingComponent()
//        }
//    }
//
//
//
//
////        val title: String = if (addressList.elementAt(2).featureName != null) {
////            addressList.elementAt(2).featureName
////        } else {
////            "New York City"
////        }
//
//
//
//
////            requestPermissionLauncher =
////                registerForActivityResult(
////                    ActivityResultContracts.RequestPermission()
////                ) { isGranted: Boolean ->
////                    if (isGranted) {
////                        Timber.d("Permission granted")
////                        //Wondering if this will call it twice in a row but if the permission gets granted for the first time this should allow it to continue to update the weather
////                        //instead of making the user click the button twice
////                        refreshLocation()
////                    } else {
////                        Timber.d("Permission denied")
////                        Toast.makeText(
////                            context,
////                            "The app will not work without location services",
////                            Toast.LENGTH_LONG
////                        ).show()
////                    }
////                }
//
//}
//
//
//@Composable
//fun SetStatusBar() {
//    val systemUiController = rememberSystemUiController()
//    val useDarkIcons = MaterialTheme.colors.isLight
//    val color = MaterialTheme.colors.primary
//    SideEffect {
//        // Update all of the system bar colors to be transparent, and use
//        // dark icons if we're in light theme
//        systemUiController.setSystemBarsColor(
//            color = color,
//            darkIcons = useDarkIcons
//        )
//    }
//}
//
//
//@ExperimentalMaterialApi
//@Composable
//fun MainWeatherComponent(weatherState: OneCall, geocoder: Geocoder, weatherViewModel: WeatherViewModel,fusedLocationProviderClient: FusedLocationProviderClient) {
//
//
//    val addressList = geocoder.getFromLocation(weatherState.lat, weatherState.lon, 5)
//    Timber.d("Address List")
//    Timber.d(weatherState.lat.toString() + "lat")
//    Timber.d(weatherState.lon.toString() + "lon")
//    Timber.d(addressList.toString())
//    val title = getTitle(addressList)
//
////        val title: String = if (addressList.elementAt(2).featureName != null) {
////            addressList.elementAt(2).featureName
////        } else {
////            "New York City"
////        }
//
//    SetStatusBar()
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        //verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TopRow(title,weatherViewModel,fusedLocationProviderClient)
//        CurrentCard(weatherState)
//        DailyCard(weatherState)
//        Spacer(
//            modifier = Modifier.height(16.dp)
//        )
//
//    }
//}
//
//private fun getTitle(addressList: List<Address>): String {
//    var x = 0
//    var numeric = true
//    var title: String = "New York City"
//
//    while (numeric) {
//        title = addressList[x].featureName
//        x++
//        numeric = title.matches(".*\\d.*".toRegex())
//        Timber.d(numeric.toString() + "" + title)
//    }
//
//    return title
//}
//
//@Composable
//fun TopRow(cityName: String, weatherViewModel: WeatherViewModel,fusedLocationProviderClient: FusedLocationProviderClient) {
//    val context = LocalContext.current
//
//    TopAppBar(
//        title = { Text(cityName) },
//        backgroundColor = MaterialTheme.colors.primary,
//        actions = {
//            IconButton(onClick = {
//                refreshLocation(weatherViewModel,fusedLocationProviderClient)
//                Toast.makeText(context, "Updating..", Toast.LENGTH_SHORT).show()
//            }) {
//                Icon(Icons.Filled.LocationOn, contentDescription = "Refresh Current Location")
//            }
//            IconButton(onClick = { goToSettings() }) {
//                Icon(Icons.Filled.Settings, contentDescription = "Go to Settings Menu")
//            }
//        },
//
//        )
//}
//
//
//@Composable
//fun MinutelyCard(weatherState: OneCall) {
//
//}
//
//@Composable
//fun HourlyCard(weatherState: OneCall) {
//
//}
//
//
//@ExperimentalMaterialApi
//@Composable
//fun DailyCard(weatherState: OneCall) {
//    val daily = weatherState.daily
//    val offSet = weatherState.offset
//
//    LazyColumn(modifier = Modifier.fillMaxWidth()) {
//        items(daily) { day ->
//            DailyRow(day, offSet)
//        }
//    }
//}
//
//
////@TODO:Maybe some sort of long press somewhere that sets all expands to false?
//@ExperimentalMaterialApi
//@Composable
//private fun DailyRow(day: Daily, offset: Int) {
//    var expanded by remember { mutableStateOf(false) }
//
//    val high = day.temp.max.roundToInt().toString()
//    val low = day.temp.min.roundToInt().toString()
//
//    val mornTemp = day.temp.morn.roundToInt().toString()
//    val dayTemp = day.temp.day.roundToInt().toString()
//    val eveTemp = day.temp.eve.roundToInt().toString()
//    val nightTemp = day.temp.night.roundToInt().toString()
//
//    val weather = day.weather[0]
//    val icon = getIconLarge(weather.icon)
//
//    val rain = day.rain
//    val snow = day.snow
//    val pop = day.pop.times(100)
//
//    val context = LocalContext.current
//
//    Timber.d(icon + "   ICON LINK")
//
//    Card(modifier = Modifier.fillMaxWidth()
//        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp).animateContentSize(),
//        shape = RoundedCornerShape(20.dp),
//        onClick = {
//            expanded = !expanded
//            Timber.d(expanded.toString())
//        }) {
//        Column() {
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(8.dp),
//                horizontalArrangement = Arrangement.Start,
//                verticalAlignment = Alignment.CenterVertically,
//
//                ) {
//                Text(
//                    text = getDayFromUnix(day.dt, offset),
//                    fontSize = 24.sp,
//
//                    )
//                Image(
//                    painter = rememberImagePainter(icon),
//                    contentDescription = stringResource(R.string.rain_icon_description),
//                    modifier = Modifier.size(64.dp)
//                )
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(8.dp),
//                    horizontalArrangement = Arrangement.End,
//                    verticalAlignment = Alignment.CenterVertically,
//                )
//                {
//                    Text(
//                        text = "$low$DEGREE_SYMBOL",
//                        modifier = Modifier.padding(8.dp, 4.dp),
//                        fontSize = 18.sp
//                    )
//                    Text(
//                        text = "$high$DEGREE_SYMBOL",
//                        modifier = Modifier.padding(8.dp, 4.dp),
//                        fontSize = 18.sp
//                    )
//                }
//            }
//            if (expanded) {
//                //Specific Temp row. Columns used to stack the elements evenly
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(8.dp),
//                    horizontalArrangement = Arrangement.Start,
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Column(
//                        modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    )
//                    {
//                        Text(
//                            text = "$mornTemp$DEGREE_SYMBOL",
//                            modifier = Modifier,
//                            fontSize = 18.sp
//                        )
//                        Text(
//                            text = "Morning",
//                            modifier = Modifier,
//                            fontSize = 18.sp
//                        )
//                    }
//                    Column(
//                        modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "$dayTemp$DEGREE_SYMBOL",
//                            modifier = Modifier,
//                            fontSize = 18.sp
//                        )
//                        Text(
//                            text = "Day",
//                            modifier = Modifier,
//                            fontSize = 18.sp
//                        )
//                    }
//                    Column(
//                        modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "$eveTemp$DEGREE_SYMBOL",
//                            modifier = Modifier,
//                            fontSize = 18.sp
//                        )
//                        Text(
//                            text = "Evening",
//                            modifier = Modifier,
//                            fontSize = 18.sp
//                        )
//                    }
//                    Column(
//                        modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "$nightTemp$DEGREE_SYMBOL",
//                            modifier = Modifier,
//                            fontSize = 18.sp
//                        )
//                        Text(
//                            text = "Night",
//                            modifier = Modifier,
//                            fontSize = 18.sp
//                        )
//                    }
//
//                }
//                //This will be the other types of things like rainfall/snowfall etc
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(8.dp),
//                    horizontalArrangement = Arrangement.Start,
//                    verticalAlignment = Alignment.CenterVertically,
//                )
//                {
//                    if (rain > 0.0) {
//                        Column(
//                            modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Image(
//                                painter = rememberImagePainter(RAIN_ICON_NIGHT),
//                                contentDescription = stringResource(R.string.rain_icon_description),
//                                modifier = Modifier.clickable(onClick = {
//                                    Toast.makeText(
//                                        context,
//                                        R.string.rain_icon_description,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }).size(64.dp)
//                            )
//                            Text(
//                                text = "${toInches(rain)} in.",
//                                modifier = Modifier.padding(4.dp),
//                                fontSize = 16.sp
//                            )
//                        }
//                    }
//                    if (snow > 0.0) {
//                        Column(
//                            modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//
//                            Image(
//                                painter = rememberImagePainter(SNOW_ICON_NIGHT),
//                                contentDescription = stringResource(R.string.snow_icon_description),
//                                modifier = Modifier.clickable(onClick = {
//                                    Toast.makeText(
//                                        context,
//                                        R.string.snow_icon_description,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }).size(64.dp)
//                            )
//                            Text(
//                                text = "${toInches(snow)} in.",
//                                modifier = Modifier.padding(4.dp),
//                                fontSize = 16.sp
//                            )
//                        }
//                    }
//                    if (pop > 0.0) {
//
//                        Column(
//                            modifier = Modifier.padding(8.dp, 4.dp).weight(1.0f),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Image(
//                                painter = rememberImagePainter(R.drawable.pop),
//                                contentDescription = stringResource(R.string.pop_icon_description),
//                                modifier = Modifier.clickable(onClick = {
//                                    Toast.makeText(
//                                        context,
//                                        R.string.pop_icon_description,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }).size(64.dp)
//                            )
//                            Text(
//                                text = "${pop.roundTo(2)} %",
//                                modifier = Modifier.padding(4.dp),
//                                fontSize = 16.sp
//                            )
//                        }
//                    }
//                }
//
//            }
//        }
//    }
//
//}
//
///**
// * Maybe make this expandable as well? And display hourly information here? Probably wont be very useful if it shows hours that already past though
// * Also add alerts here maybe as a clickable dialog or something
// */
//@Composable
//fun CurrentCard(weatherState: OneCall) {
//
//    val current = weatherState.current
//    val alerts = weatherState.alerts
//    var openDialog by remember { mutableStateOf(false) }
//
//    val temp = current.temp.roundToInt()
//    val feelsLike = current.feels_like.roundToInt()
//    val humidity = current.humidity
//    val clouds = current.clouds
//    val windSpeed = current.wind_speed
//
//    val rainObject = current.rain ?: Rain(0.0, 0.0)
//    val snowObject = current.snow ?: Snow(0.0, 0.0)
//
//    val rain = rainObject.oneHour
//    val snow = snowObject.oneHour
//
//    Timber.d("$rain + currentCardRain")
//    val timezone = weatherState.timezone
//    val context = LocalContext.current
//
//    if (openDialog) {
//
//        AlertDialog(
//            onDismissRequest = {
//                // Dismiss the dialog when the user clicks outside the dialog or on the back
//                // button. If you want to disable that functionality, simply use an empty
//                // onCloseRequest.
//                openDialog = false
//            },
////                title = {
////                    Text(text = "Weather Alerts")
////                },
//            text = {
//                Column() {
//                    for (alert in alerts) {
//                        Text(
//                            text = alert.event,
//                            fontSize = 32.sp
//                        )
//
//                        Text(
//                            text = alert.description,
//                            fontSize = 16.sp
//                        )
//                        Spacer(modifier = Modifier.size(8.dp))
//                    }
//                }
//
//
//            },
//            confirmButton = {
//                Button(
//
//                    onClick = {
//                        openDialog = false
//                    }) {
//                    Text("Thanks Bro")
//                }
//            })
////                dismissButton = {
////                    Button(
////
////                        onClick = {
////                            openDialog = false
////                        }) {
////                        Text("D")
////                    }
////                })
//    }
//
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(16.dp),
//        shape = RoundedCornerShape(20.dp)
//    ) {
//        Column(
//            //Column modifiers go here
//
//        ) {
//            Text(
//                text = "$temp$DEGREE_SYMBOL",
//                modifier = Modifier.align(CenterHorizontally).padding(8.dp),
//                fontSize = 64.sp,
//
//                )
//            if (temp != feelsLike) {
//                Text(
//                    text = "Feels like $feelsLike$DEGREE_SYMBOL",
//                    modifier = Modifier.align(CenterHorizontally).padding(8.dp),
//                    fontSize = 24.sp
//                )
//            }
//            Row(
//                //Row Modifiers go here
//                modifier = Modifier.align(CenterHorizontally),
//                horizontalArrangement = Arrangement.Start,
//                verticalAlignment = Alignment.CenterVertically
//
//            ) {
//                if (rain > 0.0) {
//                    Image(
//                        painter = rememberImagePainter(RAIN_ICON_NIGHT),
//                        contentDescription = stringResource(R.string.rain_icon_description),
//                        modifier = Modifier.clickable(onClick = {
//                            Toast.makeText(
//                                context,
//                                R.string.rain_icon_description,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }).size(64.dp)
//                    )
//                    Text(
//                        text = "${toInches(rain)} in.",
//                        modifier = Modifier.padding(4.dp),
//                        fontSize = 16.sp
//                    )
//                }
//                if (snow > 0.0) {
//                    Image(
//                        painter = rememberImagePainter(SNOW_ICON_NIGHT),
//                        contentDescription = stringResource(R.string.snow_icon_description),
//                        modifier = Modifier.clickable(onClick = {
//                            Toast.makeText(
//                                context,
//                                R.string.snow_icon_description,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }).size(64.dp)
//                    )
//                    Text(
//                        text = "${toInches(snow)} in.",
//                        modifier = Modifier.padding(4.dp),
//                        fontSize = 16.sp
//                    )
//                }
//                when (clouds.toInt()) {
//                    in 0..25 -> {
//                        Image(
//                            painter = rememberImagePainter(FEW_CLOUDS_NIGHT),
//                            contentDescription = stringResource(R.string.cloud_icon_description),
//                            modifier = Modifier.clickable(onClick = {
//                                Toast.makeText(
//                                    context,
//                                    R.string.cloud_icon_description,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }).size(64.dp)
//                        )
//                    }
//                    in 25..50 -> {
//                        Image(
//                            painter = rememberImagePainter(SCATTERED_CLOUDS_NIGHT),
//                            contentDescription = stringResource(R.string.cloud_icon_description),
//                            modifier = Modifier.clickable(onClick = {
//                                Toast.makeText(
//                                    context,
//                                    R.string.cloud_icon_description,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }).size(64.dp)
//                        )
//                    }
//                    in 50..100 -> {
//                        Image(
//                            painter = rememberImagePainter(OVERCAST_CLOUDS_NIGHT),
//                            contentDescription = stringResource(R.string.cloud_icon_description),
//                            modifier = Modifier.clickable(onClick = {
//                                Toast.makeText(
//                                    context,
//                                    R.string.cloud_icon_description,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }).size(64.dp)
//
//                        )
//                    }
//                }
//                Text(
//                    text = "$clouds%",
//                    modifier = Modifier.padding(8.dp),
//                    fontSize = 16.sp
//                )
//                Image(
//                    painter = rememberImagePainter(R.drawable.humidity200xx),
//                    contentDescription = stringResource(R.string.humidity_icon_description),
//                    modifier = Modifier.clickable(onClick = {
//                        Toast.makeText(
//                            context,
//                            R.string.humidity_icon_description,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }).size(64.dp)
//                )
//                Text(
//                    text = "$humidity%",
//                    modifier = Modifier.padding(8.dp),
//                    fontSize = 16.sp
//                )
//                //Except this isnt always true and its crashed because it was null when there were no alerts soooo
//                if (alerts !== null) {
//                    Image(
//                        painter = rememberImagePainter(R.drawable.outline_warning_white_24),
//                        contentDescription = stringResource(R.string.humidity_icon_description),
//                        modifier = Modifier.clickable(onClick = {
//                            openDialog = true
//                        }).size(40.dp)
//                    )
//
//                }
//            }
//        }
//    }
//
//}
//
//
//@Composable
//fun LiveDataLoadingComponent() {
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
//    }
//}
//
//
//private fun goToSettings() {
//
//}
//
//
//
//
//
