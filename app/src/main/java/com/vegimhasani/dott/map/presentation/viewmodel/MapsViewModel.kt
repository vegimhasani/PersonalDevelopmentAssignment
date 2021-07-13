package com.vegimhasani.dott.map.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.vegimhasani.dott.BuildConfig
import com.vegimhasani.dott.common.data.service.model.request.LatitudeLongitudeRequest
import com.vegimhasani.dott.common.data.service.model.request.RequestModel
import com.vegimhasani.dott.map.domain.model.LatitudeLongitude
import com.vegimhasani.dott.map.domain.model.UserNearbyRestaurantsState
import com.vegimhasani.dott.map.domain.usecases.GetNearbyRestaurants
import com.vegimhasani.dott.map.presentation.ui.state.MapUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getNearbyRestaurants: GetNearbyRestaurants
) : ViewModel() {

    private val _uiState: MutableStateFlow<MapUiState> = MutableStateFlow(MapUiState.Loading)

    val uiState: StateFlow<MapUiState> = _uiState

    fun onLocationRetrieved(userLocation: LatLng, latLngBounds: LatLngBounds) {
        // First we check if there are restaurants in the cache that were load previously and their latitude and longitude is within the bounds of the users viewport
        val cachedRestaurants = getNearbyRestaurants.getCachedRestaurants().values.filter { restaurant->
            latLngBounds.contains(LatLng(restaurant.latitudeLongitude.lat, restaurant.latitudeLongitude.lng))
        }
        if (cachedRestaurants.isNotEmpty()) {
            val latitudeLongitude = LatitudeLongitude(userLocation.latitude, userLocation.longitude)
            val state = MapUiState.LocationRetrieved(latitudeLongitude, cachedRestaurants)
            _uiState.value = state
        } else {
            fetchRemoteData(userLocation)
        }
    }

    private fun fetchRemoteData(latlng: LatLng) {
        viewModelScope.launch {
            val requestModel = createRequestModel(latlng.latitude, latlng.longitude)
            when (val response = getNearbyRestaurants.getNearbyRestaurants(LatitudeLongitude(latlng.latitude, latlng.longitude), requestModel)) {
                is UserNearbyRestaurantsState.Failed -> _uiState.value = MapUiState.Error
                is UserNearbyRestaurantsState.Success -> {
                    val restaurants = response.userNearbyRestaurants.restaurant
                    val latitudeLongitude = response.userNearbyRestaurants.latitudeLongitude
                    _uiState.value = MapUiState.LocationRetrieved(latitudeLongitude, restaurants)
                }
            }
        }
    }

    private fun createRequestModel(latitude: Double, longitude: Double): RequestModel {
        val categoryId = "4d4b7105d754a06374d81259"
        return RequestModel(
            LatitudeLongitudeRequest(latitude, longitude),
            BuildConfig.FOURSQUARE_CLIENT_ID,
            BuildConfig.FOURSQUARE_CLIENT_SECRET,
            BuildConfig.FOURSQUARE_VERSION,
            categoryId
        )
    }
}