package com.vegimhasani.lightyear.details.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vegimhasani.lightyear.details.presentation.state.DetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LightyearViewModel : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState: MutableStateFlow<DetailsUiState> = MutableStateFlow(DetailsUiState.Loading)

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<DetailsUiState> = _uiState
}