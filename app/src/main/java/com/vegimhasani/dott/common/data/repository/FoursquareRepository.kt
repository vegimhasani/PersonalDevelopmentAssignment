package com.vegimhasani.dott.common.data.repository

import com.vegimhasani.dott.common.data.service.FoursquareService
import com.vegimhasani.dott.common.data.service.model.request.RequestModel
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponse
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponseState
import com.vegimhasani.dott.common.data.service.model.response.RestaurantModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FoursquareRepository @Inject constructor(
    private val service: FoursquareService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val cachedRestaurants: HashMap<String, RestaurantModel> = HashMap()

    suspend fun getAllRestaurants(request: RequestModel): FoursquareResponseState = withContext(dispatcher) {

        try {
            val response = service.getRestaurantsNearby(
                request.clientID, request.clientSecret, request.latlng, request.version, request.categoryId
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                updateCache(body)
                FoursquareResponseState.Success(body)
            } else {
                FoursquareResponseState.Error
            }
        } catch (ex: Exception) {
            FoursquareResponseState.Error
        }
    }

    private fun updateCache(body: FoursquareResponse) {
        body.response.venues.forEach { venue ->
            val restaurant =
                RestaurantModel(venue.id, venue.name, venue.location.lat, venue.location.lng, venue.location.formattedAddress)
            cachedRestaurants[venue.id] = restaurant
        }
    }

    fun getCachedRestaurants() = cachedRestaurants
}