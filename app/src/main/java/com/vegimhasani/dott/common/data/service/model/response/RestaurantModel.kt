package com.vegimhasani.dott.common.data.service.model.response

class RestaurantModel(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: List<String>
)