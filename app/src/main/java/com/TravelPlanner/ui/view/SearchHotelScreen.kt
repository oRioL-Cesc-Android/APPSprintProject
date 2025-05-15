package com.TravelPlanner.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.TravelPlanner.ui.viewmodel.HotelViewModel

@Composable
fun SearchHotelsScreen(viewModel: HotelViewModel = hiltViewModel()) {
    var city by remember { mutableStateOf("London") }
    var startDate by remember { mutableStateOf("2025-05-20") }
    var endDate by remember { mutableStateOf("2025-05-25") }

    val availability by viewModel.availability.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Buscar hoteles", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Ciudad (London, Paris, Barcelona)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Fecha inicio") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("Fecha fin") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.searchAvailability("grupo05", startDate, endDate, city)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar disponibilidad")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            loading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
            availability.isEmpty() -> {
                Text("No hay disponibilidad.")
            }
            else -> {
                LazyColumn {
                    items(availability) { item ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Hotel ID: ${item.hotelId}")
                                Text("Tipo habitación: ${item.roomType}")
                                Text("Disponibles: ${item.availableRooms}")
                            }
                        }
                    }
                }
            }
        }
    }
}
