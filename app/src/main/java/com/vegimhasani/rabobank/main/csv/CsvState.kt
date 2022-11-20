package com.vegimhasani.rabobank.main.csv

sealed class CsvState {
    object Error : CsvState()
    data class Success(val csvData: List<CsvFormattedData>): CsvState()
}