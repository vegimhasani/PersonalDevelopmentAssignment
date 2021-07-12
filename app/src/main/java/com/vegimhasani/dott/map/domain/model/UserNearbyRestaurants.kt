package com.vegimhasani.dott.map.domain.model

data class UserNearbyRestaurants(
    val latitudeLongitude: LatitudeLongitude,
    val restaurant: List<Restaurant>
)