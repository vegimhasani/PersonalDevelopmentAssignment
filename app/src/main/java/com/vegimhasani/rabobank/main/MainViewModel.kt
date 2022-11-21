package com.vegimhasani.rabobank.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vegimhasani.rabobank.main.csv.CsvReader
import com.vegimhasani.rabobank.main.csv.CsvState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val csvReader: CsvReader
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)

    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch {
            downloadCsvData()
        }
    }


    private suspend fun downloadCsvData() {
        when (val csvParsedData = csvReader.downloadAndParseCsvData()) {
            is CsvState.Error -> _uiState.value = MainUiState.Error
            is CsvState.Success -> _uiState.value = MainUiState.Success(csvParsedData.csvData)
        }
    }

}
