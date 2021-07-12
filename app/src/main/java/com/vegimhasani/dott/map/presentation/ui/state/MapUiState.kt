package com.vegimhasani.dott.map.presentation.ui.state

import com.vegimhasani.dott.map.domain.model.LatitudeLongitude
import com.vegimhasani.dott.map.domain.model.Restaurant

sealed class MapUiState {
    object Loading : MapUiState()
    object Error : MapUiState()
    data class LocationRetrieved(val latitudeLongitude: LatitudeLongitude, val restaurants: List<Restaurant>) : MapUiState()
}