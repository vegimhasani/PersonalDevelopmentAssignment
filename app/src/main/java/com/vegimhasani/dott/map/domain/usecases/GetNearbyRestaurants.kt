package com.vegimhasani.dott.map.domain.usecases

import com.vegimhasani.dott.common.data.repository.FoursquareRepository
import com.vegimhasani.dott.common.data.service.model.request.RequestModel
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponse
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponseState
import com.vegimhasani.dott.common.data.service.model.response.RestaurantModel
import com.vegimhasani.dott.map.domain.model.LatitudeLongitude
import com.vegimhasani.dott.map.domain.model.Restaurant
import com.vegimhasani.dott.map.domain.model.UserNearbyRestaurants
import com.vegimhasani.dott.map.domain.model.UserNearbyRestaurantsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNearbyRestaurants @Inject constructor(
    private val repository: FoursquareRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getNearbyRestaurants(latLng: LatitudeLongitude, requestModel: RequestModel): UserNearbyRestaurantsState =
        withContext(dispatcher) {
            when (val response = repository.getAllRestaurants(requestModel)) {
                is FoursquareResponseState.Success -> {
                    createRestaurants(latLng, response.response)
                }
                is FoursquareResponseState.Error -> UserNearbyRestaurantsState.Failed
            }
        }

    private fun createRestaurants(latLng: LatitudeLongitude, response: FoursquareResponse): UserNearbyRestaurantsState.Success {
        val restaurants = response.response.venues.map { venue ->
            val restaurant = Restaurant(
                venue.id,
                venue.name,
                LatitudeLongitude(venue.location.lat, venue.location.lng),
                venue.location.address
            )
            restaurant
        }
        val userNearbyRestaurants = UserNearbyRestaurants(latLng, restaurants)
        return UserNearbyRestaurantsState.Success(userNearbyRestaurants)
    }

    fun getCachedRestaurants(): HashMap<String, Restaurant> {
        return repository.getCachedRestaurants().mapValues {
            it.value.toRestaurant()
        } as HashMap<String, Restaurant>
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
}