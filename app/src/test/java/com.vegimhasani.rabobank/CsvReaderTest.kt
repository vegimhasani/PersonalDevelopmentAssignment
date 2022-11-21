package com.vegimhasani.rabobank

import com.vegimhasani.rabobank.main.csv.CsvReader
import com.vegimhasani.rabobank.main.csv.CsvState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.Call
import okhttp3.Response
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CsvReaderTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val call: Call = mockk(relaxed = true)

    private val csvReader = CsvReader(call)

    @Test
    fun test_error_state() {
        runTest {
            val response: Response = mockk(relaxed = true)
            every {
                call.execute()
            } returns response
            every {
                response.body
            } returns null
            val csvData = csvReader.downloadAndParseCsvData()
            assert(csvData is CsvState.Error)
        }
    }
}