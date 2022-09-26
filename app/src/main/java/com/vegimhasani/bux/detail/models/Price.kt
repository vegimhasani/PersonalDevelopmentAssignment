package com.vegimhasani.bux.detail.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Price(
    @field:Json(name = "currency") val currency: String,
    @field:Json(name = "decimals") val decimals: Int,
    @field:Json(name = "amount") val amount: String
)