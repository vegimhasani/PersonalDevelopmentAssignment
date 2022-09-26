package com.vegimhasani.bux.detail.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsResponse(
    @field:Json(name = "symbol") val symbol: String,
    @field:Json(name = "securityId") val securityId: String,
    @field:Json(name = "displayName") val displayName: String,
    @field:Json(name = "currentPrice") val currentPrice: Price,
    @field:Json(name = "closingPrice") val closingPrice: Price
)