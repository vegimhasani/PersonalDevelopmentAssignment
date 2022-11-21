package com.vegimhasani.rabobank

import com.vegimhasani.rabobank.main.MainUiState
import com.vegimhasani.rabobank.main.MainViewModel
import com.vegimhasani.rabobank.main.csv.CsvReader
import com.vegimhasani.rabobank.main.csv.CsvState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val csvReader: CsvReader = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun test_error_state() = runTest { // Uses Main’s scheduler
        val viewModel = MainViewModel(csvReader)
        coEvery {
            csvReader.downloadAndParseCsvData()
        } returns CsvState.Error
        val state = viewModel.uiState.value
        assert(state is MainUiState.Error)
    }
}