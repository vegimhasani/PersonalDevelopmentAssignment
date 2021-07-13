package com.vegimhasani.dott.common.data.service

import com.vegimhasani.dott.common.data.service.model.request.LatitudeLongitudeRequest
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface FoursquareService {
    @GET("v2/venues/search")
    suspend fun getRestaurantsNearby(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("ll") latLng: LatitudeLongitudeRequest,
        @Query("radius") radius: String,
        @Query("v") versionNumber: String,
        @Query("categoryId") categoryId: String,
    ): Response<FoursquareResponse>
}