package com.vegimhasani.dott.common.data.service.model.request

data class LatitudeLongitudeRequest(
    val lat: Double,
    val lng: Double
) {
    override fun toString(): String {
        return String.format("%.1f,%.1f", lat, lng)
    }
}