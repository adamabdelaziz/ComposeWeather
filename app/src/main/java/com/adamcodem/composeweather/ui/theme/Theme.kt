package com.adamcodem.composeweather.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.adamcodem.composeweather.ui.common.Dimensions
import com.adamcodem.composeweather.ui.common.smallDimensions

private val DarkColorPalette = darkColors(
    primary = primaryDark,
    primaryVariant = primaryVariantDark,
    secondary = secondaryDark,
    secondaryVariant = secondaryDark,
    onBackground = White,
    onSurface = White,
    background = backgroundDark,
    surface = surfaceDark,


    )

private val LightColorPalette = lightColors(
    primary = primaryLight,
    primaryVariant = primaryVariantLight,
    secondary = secondaryLight,
    secondaryVariant = secondaryLight,
    onBackground = Black,
    onSurface = Black,
    background = backgroundLight,
    surface = surfaceLight,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposeWeatherTheme(
    lightTheme: Boolean,
    dimensions: Dimensions,
    content: @Composable() () -> Unit,
) {


    val colors = if (lightTheme) {
        LightColorPalette
    } else {
        DarkColorPalette
    }
    val typography = if (lightTheme) {
        if (dimensions == smallDimensions) {
            smallTypography
        } else {
            Typography
        }
    } else {
        if(dimensions == smallDimensions){
            smallTypographyDark
        }else{
            TypographyDark
        }

    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}
