package com.vegimhasani.rabobank.main.csv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import java.io.InputStream
import java.time.Clock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CsvReader @Inject constructor(private val call: Call) {

    suspend fun downloadAndParseCsvData(): CsvState = withContext(Dispatchers.IO) {
        val response = call.execute()
        val csvData = response.body?.byteStream()?.let { readCsv(it) }
        if (csvData.isNullOrEmpty()) {
            CsvState.Error
        } else {
            CsvState.Success(csvData)
        }
    }

    private fun readCsv(inputStream: InputStream): List<CsvFormattedData> {
        val reader = inputStream.bufferedReader()
        val header = reader.readLines()
        return header
            .filter { it.isNotBlank() }
            .subList(1, header.size)
            .map {
                val (firstName, lastName, _, dateOfBirth, _) = it.split(',', ignoreCase = false)
                CsvData(firstName, lastName, dateOfBirth).formatCsvData()
            }
    }

     private fun CsvData.formatCsvData(): CsvFormattedData {
        val formattedDate = dob.substring(1, dob.length - 1)
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateOfBirth = LocalDate.parse(formattedDate, dateFormat)
        val ageInYears = dateOfBirth.getAgeInYears()
        return CsvFormattedData(firstName.substring(1, firstName.length - 1), lastName.substring(1, lastName.length - 1), ageInYears)
    }

    private fun LocalDate.getAgeInYears(clock: Clock = Clock.systemUTC()): Int =
        ChronoUnit.YEARS.between(this, LocalDate.now(clock)).toInt()
}