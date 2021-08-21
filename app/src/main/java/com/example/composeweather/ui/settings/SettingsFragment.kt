package com.example.composeweather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.composeweather.R
import com.example.composeweather.preference.WeatherPreferences
import com.example.composeweather.ui.common.Dimensions
import com.example.composeweather.ui.common.LiveDataLoadingComponent
import com.example.composeweather.ui.common.regularDimensions
import com.example.composeweather.ui.common.smallDimensions
import com.example.composeweather.ui.theme.ComposeWeatherTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@ExperimentalMaterialApi
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var dimensions: Dimensions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            val prefLiveData = settingsViewModel.prefs
//            val prefFlow = settingsViewModel.preferencesFlow
            setContent {
                val configuration = LocalConfiguration.current
                dimensions =
                    if (configuration.densityDpi >= 420) smallDimensions else regularDimensions
                val prefs by prefLiveData.observeAsState(initial = prefLiveData.value)
//                val prefFlows by prefFlow.collectAsState(initial = prefFlow.first())

                if (prefs != null) {
                    val lightTheme = prefs!!.lightTheme
                    Timber.d(prefs.toString() + " weatherPreferences not null")
                    ComposeWeatherTheme(lightTheme, dimensions) {
                        SettingsScreen(prefs!!)
                    }
                } else {
                    Timber.d(prefs.toString() + " weatherPreferences null")
                    ComposeWeatherTheme(false, dimensions) {
                        LiveDataLoadingComponent()
                    }
                }
            }
        }
    }

    @Composable
    fun SettingsScreen(preferences: WeatherPreferences) {

        SetStatusBar()
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsTopRow(dimensions)
            SettingsList(preferences)
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
    fun SettingsList(preferences: WeatherPreferences) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Timber.d(MaterialTheme.colors.toString() + " materialTheme")
            Timber.d(preferences.celsiusEnabled.toString())
            Spacer(
                modifier = Modifier.height(dimensions.sixteen)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
            )
            {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(dimensions.eight),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Text(
                        style = MaterialTheme.typography.h5,
                        text = stringResource(R.string.useCelsiusString),

                        )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(dimensions.eight),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Switch(preferences.celsiusEnabled, onCheckedChange = {
                            settingsViewModel.onCelsiusSettingSelected(it)
                        })
                    }
                }
                Divider(
                    modifier = Modifier.padding(
                        0.dp,
                        dimensions.eight,
                        dimensions.eight,
                        dimensions.two
                    ),
                    thickness = 2.dp,
                    color = MaterialTheme.colors.secondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(dimensions.eight),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Text(
                        style = MaterialTheme.typography.h5,
                        text = stringResource(R.string.enableLightThemeString),

                        )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(dimensions.eight),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Switch(preferences.lightTheme, onCheckedChange = {
                            settingsViewModel.onLightThemeSettingSelected(it)

                        })
                    }
                }
                Spacer(
                    modifier = Modifier.height(dimensions.sixteen)
                )

            }

        }
    }
}