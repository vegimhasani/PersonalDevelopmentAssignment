package com.vegimhasani.dott.details.presentation.state

import com.vegimhasani.dott.map.domain.model.Restaurant

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class DisplayDetailsData(val restaurant: Restaurant) : DetailsUiState()
}