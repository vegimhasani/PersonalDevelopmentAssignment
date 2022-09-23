package com.vegimhasani.bux.sockets.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WebSocketResponseBody(
    @Json(name = "t") val t: String,
    @Json(name = "body") val body: PriceBody
)
