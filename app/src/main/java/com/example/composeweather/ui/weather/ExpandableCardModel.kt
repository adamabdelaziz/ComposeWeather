package com.example.composeweather.ui.weather

import androidx.compose.runtime.Immutable
import com.example.composeweather.domain.model.Daily

@Immutable
data class ExpandableCardModel(val id: Int, val title: String, val day:Daily)
