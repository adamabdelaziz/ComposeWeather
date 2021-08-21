package com.example.composeweather.ui.weather

import android.widget.Toast
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.composeweather.ui.common.Dimensions

@Composable
fun TopRow(
    title: String,
    dimensions: Dimensions,
    refreshLocation: () -> Unit,
    goToSettings: () -> Unit,
) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(
                    dimensions.zero,
                    dimensions.zero,
                    dimensions.zero,
                    dimensions.eight
                )
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
