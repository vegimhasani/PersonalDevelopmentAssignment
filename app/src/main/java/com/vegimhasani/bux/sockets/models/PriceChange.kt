package com.vegimhasani.bux.sockets.models

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vegimhasani.bux.detail.models.Price

@JsonClass(generateAdapter = true)
data class PriceChange(
    @Json(name = "t") val tradingQuote: String,
    @Json(name = "body") val body: PriceBody
)
