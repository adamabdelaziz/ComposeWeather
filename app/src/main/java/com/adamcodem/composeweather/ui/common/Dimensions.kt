package com.adamcodem.composeweather.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Dimensions(
    val four: Dp,
    val eight: Dp,
    val sixteen: Dp,
    val zero: Dp,
    val twenty: Dp,
    val twentyfour: Dp,
    val sixtyfour: Dp,
    val bigImage: Dp,
    val two: Dp,

    ) {
    override fun toString(): String {
        return ("${this.four},  ${this.eight},  ${this.sixteen} , ${this.bigImage}")
    }
}

val smallDimensions = Dimensions(
    four = 2.dp,
    eight = 4.dp,
    sixteen = 8.dp,
    twenty = 10.dp,
    zero = 0.dp,
    sixtyfour = 32.dp,
    twentyfour = 24.dp,
    bigImage = 48.dp,
    two = 1.dp,
)

val regularDimensions = Dimensions(
    four = 4.dp,
    eight = 8.dp,
    sixteen = 16.dp,
    zero = 0.dp,
    twenty = 20.dp,
    sixtyfour = 64.dp,
    twentyfour = 24.dp,
    bigImage = 96.dp,
    two = 2.dp,
)