package com.vegimhasani.bux.detail

sealed class DetailsState {

    object PriceChange : DetailsState()

    data class ConnectionState(val message: String) : DetailsState()
}