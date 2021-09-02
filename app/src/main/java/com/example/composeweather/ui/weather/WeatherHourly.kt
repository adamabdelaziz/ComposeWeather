package com.example.composeweather.ui.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import com.example.composeweather.R
import com.example.composeweather.domain.model.Hourly
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.domain.model.Rain
import com.example.composeweather.ui.common.Dimensions
import com.example.composeweather.util.DEGREE_SYMBOL
import com.example.composeweather.util.getDayFromUnix
import com.example.composeweather.util.getIconLarge
import com.example.composeweather.util.getTimestampFromUnix
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun HourlyColumn(
    hour: Hourly,
    offset: Double,
    dimensions: Dimensions
) {
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
            .padding(
                start = dimensions.eight,
                top = dimensions.zero,
                bottom = dimensions.zero,
                end = dimensions.eight
            ),
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
            modifier = Modifier.size(dimensions.bigImage)
            //.padding(8.dp,4.dp)
        )
        Text(
            style = MaterialTheme.typography.h5,
            text = "$temp$DEGREE_SYMBOL",
            //modifier = Modifier.padding(8.dp),

        )
    }
}


@Composable
fun HourlyCard(weatherState: OneCall, dimensions: Dimensions) {
    val hourly = weatherState.hourly
    val offset = weatherState.offset

    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .padding(
                start = dimensions.eight,
                end = dimensions.eight,
                bottom = dimensions.twentyfour,
                top = dimensions.eight
            ),

        ) {
        items(hourly) { hour ->
            val dt = hour.dt
            val timestamp = getTimestampFromUnix(dt, offset!!)
            val timestampDay: String = timestamp.substring(0, 3)
            val dayOfWeek: String = getDayFromUnix(dt, offset)
            Timber.d(timestamp.toString() + "dtStamp")

            HourlyColumn(hour, offset, dimensions)


        }
    }
}