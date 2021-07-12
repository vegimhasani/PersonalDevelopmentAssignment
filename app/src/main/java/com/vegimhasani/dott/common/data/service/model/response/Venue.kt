package com.vegimhasani.dott.common.data.service.model.response

data class Venue(
    val categories: List<Category>,
    val id: String,
    val location: Location,
    val name: String,
    val venuePage: VenuePage
)