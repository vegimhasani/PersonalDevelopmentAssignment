package com.vegimhasani.dott.map.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import com.vegimhasani.dott.BuildConfig
import com.vegimhasani.dott.common.data.service.model.request.LatitudeLongitudeRequest
import com.vegimhasani.dott.common.data.service.model.request.RequestModel
import com.vegimhasani.dott.map.domain.model.LatitudeLongitude
import com.vegimhasani.dott.map.domain.model.Restaurant
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

    fun onLocationRetrieved(userLocation: LatLng) {
        fetchRemoteData(userLocation)
    }

    fun loadMoreData(userLocation: LatLng, usersVisibleRegion: VisibleRegion) {
        displayCachedDataIfAvailable(userLocation, usersVisibleRegion)
    }

    private fun displayCachedDataIfAvailable(userLocation: LatLng, usersVisibleRegion: VisibleRegion) {
        // First we check if there are restaurants in the cache that were load previously and their latitude and longitude is within the bounds of the users viewport
        val cachedRestaurants = getNearbyRestaurants.getCachedRestaurants().values.filter { restaurant ->
            usersVisibleRegion.latLngBounds.contains(LatLng(restaurant.latitudeLongitude.lat, restaurant.latitudeLongitude.lng))
        }
        // In case we find them we displayed the cached data first
        if (cachedRestaurants.isNotEmpty()) {
            displayData(LatitudeLongitude(userLocation.latitude, userLocation.longitude), cachedRestaurants)
        } else {
            fetchRemoteData(userLocation)
        }
    }

    private fun displayData(latitudeLongitude: LatitudeLongitude, restaurants: List<Restaurant>) {
        _uiState.value = MapUiState.LocationRetrieved(latitudeLongitude, restaurants)
    }

    private fun fetchRemoteData(userLocation: LatLng) {
        viewModelScope.launch {
            val requestModel = createRequestModel(userLocation.latitude, userLocation.longitude)
            when (val response =
                getNearbyRestaurants.getNearbyRestaurants(LatitudeLongitude(userLocation.latitude, userLocation.longitude), requestModel)) {
                is UserNearbyRestaurantsState.Failed -> _uiState.value = MapUiState.Error
                is UserNearbyRestaurantsState.Success -> {
                    if (response.userNearbyRestaurants.restaurant.isNotEmpty()) {
                        displayData(response.userNearbyRestaurants.latitudeLongitude, response.userNearbyRestaurants.restaurant)
                    }
                }
            }
        }
    }

    private fun createRequestModel(latitude: Double, longitude: Double): RequestModel {
        // This is the category id for restaurants based on the Foursquare API
        val categoryId = "4d4b7105d754a06374d81259"
        // Radius to limit results to venues within this many meters of the specified location
        val radiusInMeters = "500"
        return RequestModel(
            LatitudeLongitudeRequest(latitude, longitude),
            BuildConfig.FOURSQUARE_CLIENT_ID,
            BuildConfig.FOURSQUARE_CLIENT_SECRET,
            BuildConfig.FOURSQUARE_VERSION,
            categoryId,
            radiusInMeters
        )
    }
}