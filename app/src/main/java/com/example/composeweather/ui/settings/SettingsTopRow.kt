package com.example.composeweather.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composeweather.R
import com.example.composeweather.ui.common.Dimensions

@Composable
fun SettingsTopRow(dimensions: Dimensions) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, dimensions.eight)
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
    )
}