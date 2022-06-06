package com.vegimhasani.lightyear.details.presentation.state

import com.vegimhasani.lightyear.map.domain.model.Restaurant

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class DisplayDetailsData(val restaurant: Restaurant) : DetailsUiState()
}