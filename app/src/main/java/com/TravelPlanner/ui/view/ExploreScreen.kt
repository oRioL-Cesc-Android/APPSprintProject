package com.TravelPlanner.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.TravelPlanner.models.ReserveRequest
import com.TravelPlanner.ui.viewmodel.HotelViewModel

@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: HotelViewModel = hiltViewModel()
) {
    var city by remember { mutableStateOf("London") }
    var startDate by remember { mutableStateOf("2025-06-01") }
    var endDate by remember { mutableStateOf("2025-06-05") }

    val hotels by viewModel.hotels.collectAsState()
    val allHotels by viewModel.allHotels.collectAsState()
    val reservationResult = viewModel.reservationResult

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

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                viewModel.searchHotels(startDate, endDate, city)
            }) {
                Text("Search Hotels")
            }

            Button(onClick = {
                viewModel.fetchAllHotels()
            }) {
                Text("All Hotels")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        reservationResult?.let { success ->
            Text(
                text = if (success) "✅ Reservation successful!" else "❌ Reservation failed.",
                color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Search Results:", style = MaterialTheme.typography.titleMedium)

        hotels.forEach { hotel ->
            HotelItem(hotel.name, hotel.rating, hotel.address) {
                val reserveRequest = ReserveRequest(
                    hotel_id = hotel.id,
                    room_id = "room01", // Simulado
                    start_date = startDate,
                    end_date = endDate,
                    guest_name = "John Doe",
                    guest_email = "john@example.com"
                )
                viewModel.reserveRoom(reserveRequest)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Forzamos mostrar All Hotels aunque esté vacío (debugging)
        Text("All Hotels:", style = MaterialTheme.typography.titleMedium)

        allHotels.forEach { hotel ->
            HotelItem(hotel.name, hotel.rating, hotel.address) {
                val reserveRequest = ReserveRequest(
                    hotel_id = hotel.id,
                    room_id = "room01", // Simulado
                    start_date = startDate,
                    end_date = endDate,
                    guest_name = "John Doe",
                    guest_email = "john@example.com"
                )
                viewModel.reserveRoom(reserveRequest)
            }
        }
    }
}

@Composable
fun HotelItem(hotelName: String, rating: Int, address: String, onReserveClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text("🏨 $hotelName ($rating⭐)")
        Text("📍 $address")
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = onReserveClick) {
            Text("Reserve")
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}
