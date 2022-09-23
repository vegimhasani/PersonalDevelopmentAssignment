package com.vegimhasani.bux.detail.models

import com.google.gson.annotations.SerializedName


data class Price(
    @SerializedName("currency") val currency: String,
    @SerializedName("decimals") val decimals: Int,
    @SerializedName("amount") val amount: String
)