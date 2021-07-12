package com.vegimhasani.dott.common.data.service.model.response

sealed class FoursquareResponseState {
    data class Success(val response: FoursquareResponse): FoursquareResponseState()
    object Error: FoursquareResponseState()
}