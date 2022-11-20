package com.vegimhasani.rabobank.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vegimhasani.rabobank.main.csv.CsvFormattedData

@Composable
fun MainView(model: MainViewModel) {
    val uiState = model.uiState.collectAsState()
    when (uiState.value) {
        is MainUiState.Error -> Text(text = "Error loading data")
        is MainUiState.Loading -> Text(text = "Loading data")
        is MainUiState.Success -> CsvList(csvList = (uiState.value as MainUiState.Success).csvData)
    }
}

@Composable
fun CsvList(csvList: List<CsvFormattedData>) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        csvList.forEach { csvItem ->
            CsvItem(csvItem)
        }
    }
}

@Composable
fun CsvItem(csvData: CsvFormattedData) {
    Card(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(all = 10.dp)) {
            Text("${csvData.firstName}  ${csvData.lastName}", fontSize = 16.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(10.dp))
            Text("Age: ${csvData.age}", color = Gray, modifier = Modifier.padding(10.dp))
        }
    }
}