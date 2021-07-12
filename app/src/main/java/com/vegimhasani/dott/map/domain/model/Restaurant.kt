package com.vegimhasani.dott.map.domain.model

data class Restaurant(
    val id: String,
    val name: String,
    val latitudeLongitude: LatitudeLongitude,
    val address: String?
)