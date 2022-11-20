package com.vegimhasani.rabobank.main

import com.vegimhasani.rabobank.main.csv.CsvFormattedData

sealed class MainUiState {
    object Loading : MainUiState()
    object Error : MainUiState()
    data class Success(val csvData: List<CsvFormattedData>): MainUiState()
}