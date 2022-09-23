package com.vegimhasani.bux.sockets.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PriceBody(
    @Json(name = "currentPrice") val currentPrice: String,
)
