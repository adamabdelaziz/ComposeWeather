package com.example.composeweather.ui.weather

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.composeweather.R
import com.example.composeweather.domain.model.Daily
import com.example.composeweather.domain.model.OneCall
import com.example.composeweather.ui.common.Dimensions
import com.example.composeweather.util.*
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun DailyCard(
    weatherState: OneCall,
    dimensions: Dimensions
) {
    val daily = weatherState.daily
    val offset = weatherState.offset!!

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(daily) { day ->
            DailyRow(day, offset, dimensions)
        }
    }
}


//@TODO:Maybe some sort of long press somewhere that sets all expands to false?
@ExperimentalMaterialApi
@Composable
private fun DailyRow(
    day: Daily,
    offset: Double,
    dimensions: Dimensions
) {
    val context = LocalContext.current
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
        .padding(
            start = dimensions.sixteen,
            end = dimensions.sixteen,
            bottom = dimensions.eight,
            top = dimensions.eight
        ).animateContentSize(),
        shape = RoundedCornerShape(dimensions.twenty),
        onClick = {
            expanded = !expanded
            //Timber.d(expanded.toString())
        }) {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth().padding(dimensions.eight),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = getDayFromUnix(day.dt, offset),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(dimensions.eight, dimensions.four),
                    //fontSize = 24.sp,

                )
                Image(
                    painter = rememberImagePainter(icon),
                    contentDescription = stringResource(R.string.rain_icon_description),
                    modifier = Modifier.size(64.dp).padding(dimensions.eight, dimensions.four)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(dimensions.eight),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                )
                {
                    Text(
                        style = MaterialTheme.typography.h4,
                        text = "$low$DEGREE_SYMBOL",
                        modifier = Modifier.padding(dimensions.eight, dimensions.four),


                        )
                    Text(
                        style = MaterialTheme.typography.h4,
                        text = "$high$DEGREE_SYMBOL",
                        modifier = Modifier.padding(dimensions.eight, dimensions.four),
                    )
                }
            }
            if (expanded) {
                //Specific Temp row. Columns used to stack the elements evenly
                Row(
                    modifier = Modifier.fillMaxWidth().padding(dimensions.eight),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.padding(dimensions.eight, dimensions.four)
                            .weight(1.0f),
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
                        modifier = Modifier.padding(dimensions.eight, dimensions.four)
                            .weight(1.0f),
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
                        modifier = Modifier.padding(dimensions.eight, dimensions.four)
                            .weight(1.0f),
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
                        modifier = Modifier.padding(dimensions.eight, dimensions.four)
                            .weight(1.0f),
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
                    modifier = Modifier.fillMaxWidth().padding(dimensions.eight),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                )
                {
                    if (rain > 0.0) {
                        Column(
                            modifier = Modifier.padding(dimensions.eight, dimensions.four)
                                .weight(1.0f),
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
                                }).size(dimensions.sixtyfour)
                            )
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = "${toInches(rain)} in.",
                                modifier = androidx.compose.ui.Modifier.padding(dimensions.four),
                            )
                        }
                    }
                    if (snow > 0.0) {
                        Column(
                            modifier = Modifier.padding(dimensions.eight, dimensions.four)
                                .weight(1.0f),
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
                                }).size(dimensions.sixtyfour)
                            )
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = "${toInches(snow)} in.",
                                modifier = androidx.compose.ui.Modifier.padding(dimensions.four),

                                )
                        }
                    }
                    if (pop > 0.0) {

                        Column(
                            modifier = Modifier.padding(dimensions.eight, dimensions.four)
                                .weight(1.0f),
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
                                }).size(dimensions.sixtyfour)
                            )
                            Text(
                                style = MaterialTheme.typography.h5,
                                text = "${pop.roundTo(2)} %",
                                modifier = androidx.compose.ui.Modifier.padding(dimensions.four),
                            )
                        }
                    }
                }

            }
        }
    }

}