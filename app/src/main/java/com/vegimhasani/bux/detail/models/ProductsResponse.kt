package com.vegimhasani.bux.detail.models

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("securityId") val securityId: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("currentPrice") val currentPrice: Price,
    @SerializedName("closingPrice") val closingPrice: Price
)