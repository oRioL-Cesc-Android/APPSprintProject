package com.TravelPlanner.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.TravelPlanner.data.remote.dto.HotelDto
import com.TravelPlanner.ui.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: HotelViewModel = viewModel(),
    navController: NavHostController
) {
    // Observe hotels from ViewModel
    val hotelState by viewModel.hotels.collectAsState()

    // Fetch hotels when screen appears
    LaunchedEffect(key1 = HotrlgroupId) {
        viewModel.fetchHotels(groupId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Explore Hotels") })
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {

            when (val state = hotelState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    HotelList(hotels = state.data)
                }
                is UiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun HotelList(hotels: List<HotelDto>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(hotels) { hotel ->
            HotelItem(hotel)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun HotelItem(hotel: HotelDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = hotel.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "City: ${hotel.id}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Stars: ${hotel.rating}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
