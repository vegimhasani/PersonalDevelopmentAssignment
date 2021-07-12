package com.vegimhasani.dott.common.data.service.model.request

data class RequestModel(
    val latlng: LatitudeLongitudeRequest,
    val clientID: String,
    val clientSecret: String,
    val version: String,
    val categoryId: String
)