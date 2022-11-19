package com.vegimhasani.rabobank.main

import androidx.lifecycle.ViewModel
import com.vegimhasani.rabobank.main.csv.CsvReader
import com.vegimhasani.rabobank.main.csv.CsvState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val csvReader: CsvReader
) : ViewModel() {

    suspend fun getData(): CsvState = csvReader.downloadAndParseCsvData()
}
