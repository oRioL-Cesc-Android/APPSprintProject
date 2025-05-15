package com.TravelPlanner.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.TravelPlanner.ui.viewmodel.HotelViewModel

@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: HotelViewModel = hiltViewModel()
) {
    var city by remember { mutableStateOf("London") }
    var startDate by remember { mutableStateOf("2025-06-01") }
    var endDate by remember { mutableStateOf("2025-06-05") }

    val hotels = viewModel.hotels.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City (London/Paris/Barcelona)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Start Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("End Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.searchHotels("G01", startDate, endDate, city)
        }) {
            Text("Search Hotels")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Results:", style = MaterialTheme.typography.titleMedium)

        hotels.value.forEach { hotel ->
            Spacer(modifier = Modifier.height(8.dp))
            Text("🏨 ${hotel.name} (${hotel.rating}⭐)")
            Text("📍 ${hotel.address}")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

