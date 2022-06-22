package com.vegimhasani.lightyear.viewmodel

import androidx.lifecycle.ViewModel
import com.vegimhasani.lightyear.state.LightyearUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LightyearViewModel : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState: MutableStateFlow<LightyearUiState> = MutableStateFlow(LightyearUiState.Loading)

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LightyearUiState> = _uiState
}