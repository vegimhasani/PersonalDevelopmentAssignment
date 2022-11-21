package com.vegimhasani.rabobank

import com.vegimhasani.rabobank.main.csv.CsvReader
import com.vegimhasani.rabobank.main.csv.CsvState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.Call
import okhttp3.Response
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CsvReaderTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val call: Call = mock()

    private val csvReader = CsvReader(call)

    @Test
    fun test_error_state() {
        runTest {
            val response: Response = mock()
            whenever(call.execute()).thenReturn(response)
            whenever(response.body).thenReturn(null)
            val csvData = csvReader.downloadAndParseCsvData()
            assert(csvData is CsvState.Error)
        }
    }
}