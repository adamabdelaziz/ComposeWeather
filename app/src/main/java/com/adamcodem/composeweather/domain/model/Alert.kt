package com.adamcodem.composeweather.domain.model

import com.google.gson.annotations.SerializedName

data class Alert(
    @SerializedName("sender_name")
    val senderName: String,
    val event: String,
    val start: Int,
    val end: Int,
    val description: String,
    val tags: List<String>
)



