package com.vegimhasani.bux.sockets.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Subscribe(
    @Json(name = "subscribeTo") val subscribeTo: List<String>? = null,
    @Json(name = "unsubscribeFrom") val unsubscribeFrom: List<String>? = null
)