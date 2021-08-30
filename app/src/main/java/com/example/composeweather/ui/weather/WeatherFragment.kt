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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.composeweather.R
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.ui.common.Dimensions
import com.example.composeweather.ui.common.LoadingComponent
import com.example.composeweather.ui.common.regularDimensions
import com.example.composeweather.ui.common.smallDimensions
import com.example.composeweather.ui.theme.ComposeWeatherTheme
import com.example.composeweather.util.NYC_LAT
import com.example.composeweather.util.NYC_LON
import com.example.composeweather.util.OneCallEvent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@ExperimentalMaterialApi
@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var geocoder: Geocoder
    private lateinit var dimensions: Dimensions


    override fun onCreate(savedInstanceState: Bundle?) {

        Timber.d("onCreate called in WeatherFragment")

        geocoder = Geocoder(context)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.onTriggerEvent(OneCallEvent.UpdateLocation(true))
                    refreshLocation()
                    Timber.d("Permission granted")
                } else {
                    //Probably
                    viewModel.onTriggerEvent(OneCallEvent.UpdateLocation(false))
                    Timber.d("Permission denied")
                    Toast.makeText(
                        context,
                        getString(R.string.locationError),
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
                val configuration = LocalConfiguration.current
                dimensions =
                    if (configuration.densityDpi >= 420) smallDimensions else regularDimensions
                //dimensions = if(configuration.screenHeightDp <= 360) smallDimensions else regularDimensions
                Timber.d(configuration.screenHeightDp.toString() + " screenHeight configuration")
                Timber.d(dimensions.toString() + " dimensions configuration")
                Timber.d(configuration.screenWidthDp.toString() + " screenWidth configuration")
                //Will probably have to base it on DPI
                Timber.d(configuration.densityDpi.toString() + " density configuration")
                val loading = viewModel.loading.value
                val prefsLoading = viewModel.prefsLoading.value
                val location = viewModel.location.value
                val prefs = viewModel.prefs.value
                val oneCall = viewModel.oneCall.value
                //Timber.d("prefs is $prefs WeatherFragment onCreateVieww")
                //Timber.d("location is $location WeatherFragment onCreateVieww")
                if (loading || prefsLoading) {
                    if (prefs != null && oneCall != null) {
                        ComposeWeatherTheme(prefs.lightTheme, dimensions) {
                            MainWeatherComponent(oneCall)
                        }
                    }
                    LoadingComponent()
                } else {
                    if (prefs != null && oneCall != null) {
                        ComposeWeatherTheme(prefs.lightTheme, dimensions) {
                            MainWeatherComponent(oneCall)
                        }
                    }

                }
//                if (location) {
//                    if (loading || prefsLoading) {
//                        if (prefs != null && oneCall != null) {
//                            ComposeWeatherTheme(prefs.lightTheme, dimensions) {
//                                MainWeatherComponent(oneCall)
//                            }
//                        } else {
//                            LiveDataLoadingComponent()
//                        }
//                    } else if (!loading && !prefsLoading) {
//                        if (prefs != null && oneCall != null) {
//                            ComposeWeatherTheme(prefs.lightTheme, dimensions) {
//                                MainWeatherComponent(oneCall)
//                            }
//                        }
//                    }
//                }else{
//                    if (loading || prefsLoading) {
//                        if (prefs != null && oneCall != null) {
//                            ComposeWeatherTheme(prefs.lightTheme, dimensions) {
//                                MainWeatherComponent(oneCall)
//                            }
//                        } else {
//                            LiveDataLoadingComponent()
//                        }
//                    } else if (!loading && !prefsLoading) {
//                        if (prefs != null && oneCall != null) {
//                            ComposeWeatherTheme(prefs.lightTheme, dimensions) {
//                                MainWeatherComponent(oneCall)
//                            }
//                        }
//                    }
//                }

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

//    @Composable
//    fun ProvideDimensions(
//        dimensions: Dimensions,
//        content: @Composable () -> Unit
//    ) {
//        val dimensionSet = remember { dimensions }
//        CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
//    }

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
            TopRow(
                title = title,
                dimensions = dimensions,
                refreshLocation = { refreshLocation() },
                goToSettings = { goToSettings() },

                )

            CurrentCard(weatherState, dimensions)
            HourlyCard(weatherState, dimensions)
            DailyCard(weatherState, dimensions)
            Spacer(
                modifier = Modifier.height(dimensions.sixteen)
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

