package com.example.composeweather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.composeweather.R
import com.example.composeweather.preference.WeatherPreferences
import com.example.composeweather.ui.theme.ComposeWeatherTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@ExperimentalMaterialApi
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModels()


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

                val prefs by prefLiveData.observeAsState(initial = prefLiveData.value)
//                val prefFlows by prefFlow.collectAsState(initial = prefFlow.first())

                if (prefs != null) {
                    Timber.d(prefs.toString() + " weatherPreferences not null")
                    ComposeWeatherTheme {
                        SettingsScreen(prefs!!)
                    }
                } else {
                    Timber.d(prefs.toString() + " weatherPreferences null")
                    ComposeWeatherTheme {
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
            TopRow()
            SettingsList(preferences)
        }

    }

    @Composable
    fun LiveDataLoadingComponent() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator(modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally))
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
    fun TopRow() {
        TopAppBar(
            title = { Text(stringResource(R.string.settings_title)) },
            backgroundColor = MaterialTheme.colors.primary,
        )
    }

    @Composable
    fun SettingsList(preferences: WeatherPreferences) {
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Timber.d(preferences.celsiusEnabled.toString())
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
            )
            {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Text(
                        text = "Use Celsius",
                        fontSize = 24.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Switch(preferences.celsiusEnabled, onCheckedChange = {
                            settingsViewModel.onCelsiusSettingSelected(it)
                        })
                    }
                }
                Divider(
                    modifier = Modifier.padding(0.dp, 8.dp, 8.dp, 2.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colors.secondary
                )

            }
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                modifier= Modifier.weight(1f),
                fontSize = 24.sp,
                text ="Test"
            )
        }
    }
}