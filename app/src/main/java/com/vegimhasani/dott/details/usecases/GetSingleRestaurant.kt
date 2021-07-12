package com.vegimhasani.dott.details.usecases

import com.vegimhasani.dott.common.data.repository.FoursquareRepository
import com.vegimhasani.dott.common.data.service.model.response.RestaurantModel
import com.vegimhasani.dott.map.domain.model.LatitudeLongitude
import com.vegimhasani.dott.map.domain.model.Restaurant
import javax.inject.Inject

class GetSingleRestaurant @Inject constructor(private val repository: FoursquareRepository) {
    fun getRestaurantWithId(id: String): Restaurant? =
        repository.getCachedRestaurants()[id]?.toRestaurant()
}

private fun RestaurantModel.toRestaurant(): Restaurant {
    val sb = StringBuilder()
    this.address.forEach { address ->
        sb.append(address)
    }
    val formattedAddress = sb.toString()
    return Restaurant(
        this.id,
        this.name,
        LatitudeLongitude(this.latitude, this.longitude),
        formattedAddress
    )
}