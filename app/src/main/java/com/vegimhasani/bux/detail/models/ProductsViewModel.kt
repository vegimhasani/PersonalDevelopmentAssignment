package com.vegimhasani.bux.detail.models

data class ProductsViewModel(
    val displayName: String,
    val currentPriceFormatted: String,
    val closingPriceFormatted: String,
    val percentageDifference: String
)