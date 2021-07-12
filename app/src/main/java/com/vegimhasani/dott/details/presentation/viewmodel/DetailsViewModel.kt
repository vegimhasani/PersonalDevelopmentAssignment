package com.vegimhasani.dott.details.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vegimhasani.dott.details.presentation.state.DetailsUiState
import com.vegimhasani.dott.details.usecases.GetSingleRestaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getSingleRestaurant: GetSingleRestaurant
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState: MutableStateFlow<DetailsUiState> = MutableStateFlow(DetailsUiState.Loading)

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<DetailsUiState> = _uiState

    fun getRestaurant(restaurantId: String) {
        val restaurant = getSingleRestaurant.getRestaurantWithId(restaurantId)
        restaurant?.let {
            _uiState.value = DetailsUiState.DisplayDetailsData(restaurant)
        }
    }
}