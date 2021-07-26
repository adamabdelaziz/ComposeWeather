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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.composeweather.preference.WeatherPreferences
import com.example.composeweather.ui.theme.ComposeWeatherTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
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
            setContent {

                val prefs by prefLiveData.observeAsState(initial = prefLiveData.value)


                if (prefs != null) {
                    Timber.d(prefs.toString() + " weatherPreferences not null")
                    ComposeWeatherTheme {
                        SettingsList(prefs!!)
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
    fun SettingsList(preferences: WeatherPreferences) {

        SetStatusBar()
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Timber.d(preferences.celsiusEnabled.toString())
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = "Use Celsius",
                    fontSize = 24.sp,
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
            Spacer(
                modifier = Modifier.height(16.dp)
            )

        }
    }
}