package com.vegimhasani.bux.main

sealed class MainState {

    data class NavigateToDetails(val productId: String) : MainState()

    object DisplayData : MainState()
}