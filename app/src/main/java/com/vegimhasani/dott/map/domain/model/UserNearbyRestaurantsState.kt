package com.vegimhasani.dott.map.domain.model

sealed class UserNearbyRestaurantsState {
    data class Success(val userNearbyRestaurants: UserNearbyRestaurants): UserNearbyRestaurantsState()
    object Failed: UserNearbyRestaurantsState()
}