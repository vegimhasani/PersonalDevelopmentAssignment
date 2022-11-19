package com.vegimhasani.rabobank.main.csv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

class CsvReader {

    suspend fun downloadAndParseCsvData(): CsvState = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder().url("https://raw.githubusercontent.com/RabobankDev/AssignmentCSV/main/issues.csv")
            .build()
        val response = client.newCall(request).execute()
        val csvData = response.body?.byteStream()?.let { readCsv(it) }
        if (csvData.isNullOrEmpty()) {
            CsvState.Error
        } else {
            CsvState.Success(csvData)
        }
    }

    private fun readCsv(inputStream: InputStream): List<CsvData> {
        val reader = inputStream.bufferedReader()
        val header = reader.readLines()
        return header
            .filter { it.isNotBlank() }
            .map {
                val (firstName, lastName, count, dob, avatar) = it.split(',', ignoreCase = false)
                CsvData(firstName, lastName, dob)
            }.toList()
    }

}