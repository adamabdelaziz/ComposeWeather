package com.example.composeweather.ui.weather

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.composeweather.R
import com.example.composeweather.domain.model.Alert
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.domain.model.Rain
import com.example.composeweather.domain.model.Snow
import com.example.composeweather.ui.common.Dimensions
import com.example.composeweather.util.*
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun AlertWindow(
    alerts: List<Alert>,
    dimensions: Dimensions
) {
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
            Spacer(modifier = Modifier.size(dimensions.eight))
        }
    }

}

/**
 * Maybe make this expandable as well? And display hourly information here? Probably wont be very useful if it shows hours that already past though
 * Also add alerts here maybe as a clickable dialog or something
 */
@Composable
fun CurrentCard(weatherState: OneCall, dimensions: Dimensions) {
    val context = LocalContext.current
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
            text = { AlertWindow(alerts!!, dimensions) },
            confirmButton = {
                Button(

                    onClick = {
                        openDialog = false
                    }) {
                    Text(stringResource(R.string.thanks_bro))
                }
            })

    }

    Column(
        //Column modifiers go here
        modifier = Modifier.fillMaxWidth().padding(dimensions.sixteen),

        ) {
        Text(
            style = MaterialTheme.typography.h2,
            text = "$temp$DEGREE_SYMBOL",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(dimensions.eight),

            )

        Row(
            //Row Modifiers go here
            modifier = Modifier.align(Alignment.CenterHorizontally),
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
                    }).size(dimensions.sixtyfour)
                )
                Text(
                    style = MaterialTheme.typography.h5,
                    text = "${toInches(rain)} in.",
                    modifier = androidx.compose.ui.Modifier.padding(dimensions.four),
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
                    }).size(dimensions.sixtyfour)
                )
                Text(
                    style = MaterialTheme.typography.h5,
                    text = "${toInches(snow)} in.",
                    modifier = androidx.compose.ui.Modifier.padding(dimensions.four),

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
                        }).size(dimensions.sixtyfour)
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
                        }).size(dimensions.sixtyfour)
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
                        }).size(dimensions.sixtyfour)

                    )
                }
            }
            Text(
                style = MaterialTheme.typography.h5,
                text = "$clouds%",
                modifier = androidx.compose.ui.Modifier.padding(dimensions.eight),
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
                }).size(dimensions.sixtyfour)
            )
            Text(
                style = MaterialTheme.typography.h5,
                text = "$humidity%",
                modifier = androidx.compose.ui.Modifier.padding(dimensions.eight),
            )

            if (!alerts.isNullOrEmpty()) {
                Icon(
                    Icons.Rounded.Warning,
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "warning",
                    modifier = Modifier.clickable { openDialog = true }
                        .size(dimensions.sixtyfour)
                        .padding(dimensions.eight),
                )


            }

            //End of Row

        }

//                } //End of Column
    }
    //End of Card
}