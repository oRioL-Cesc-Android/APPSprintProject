package com.TravelPlanner.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.TravelPlanner.models.ReserveRequest
import com.TravelPlanner.models.Reservation
import com.TravelPlanner.ui.viewmodel.HotelViewModel

@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: HotelViewModel = hiltViewModel()
) {
    var city by remember { mutableStateOf("London") }
    var startDate by remember { mutableStateOf("2025-06-01") }
    var endDate by remember { mutableStateOf("2025-06-05") }
    var roomId by remember { mutableStateOf("R1") }
    var guestName by remember { mutableStateOf("John Doe") }
    var guestEmail by remember { mutableStateOf("john@example.com") }
    var showAllHotels by remember { mutableStateOf(false) }

    val hotels by viewModel.hotels.collectAsState()
    val allHotels by viewModel.allHotels.collectAsState()
    val reservations = viewModel.userReservations
    val reservationResult = viewModel.reservationResult
    val reservationListStatus = viewModel.listReservationResult

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BackButton { navController.popBackStack() }

        SearchInputs(
            city = city, onCityChange = { city = it },
            startDate = startDate, onStartDateChange = { startDate = it },
            endDate = endDate, onEndDateChange = { endDate = it },
            roomId = roomId, onRoomIdChange = { roomId = it },
            guestName = guestName, onGuestNameChange = { guestName = it },
            guestEmail = guestEmail, onGuestEmailChange = { guestEmail = it }
        )

        ActionButtons(
            onFetchAll = {
                viewModel.fetchAllHotels()
                showAllHotels = true
            },
            onShowReservations = {
                viewModel.listReservation(guestEmail)
                showAllHotels = false
            }
        )

        reservationResult?.let { success ->
            Text(
                text = if (success) "✅ Reserva exitosa" else "❌ Error al reservar",
                color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        reservationListStatus?.let {
            Text(
                text = it,
                color = if (it.startsWith("✅")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        if (hotels.isNotEmpty()) {
            SectionTitle("Resultados de búsqueda")
            hotels.forEach { hotel ->
                HotelItem(
                    hotelName = hotel.name,
                    rating = hotel.rating,
                    address = hotel.address,
                    imageUrl = hotel.image_url
                ) {
                    viewModel.reserveRoom(
                        ReserveRequest(hotel.id, roomId, startDate, endDate, guestName, guestEmail)
                    )
                }
            }
        }

        if (showAllHotels && allHotels.isNotEmpty()) {
            SectionTitle("Todos los hoteles")
            allHotels.forEach { hotel ->
                HotelItem(
                    hotelName = hotel.name,
                    rating = hotel.rating,
                    address = hotel.address,
                    imageUrl = hotel.image_url
                ) {
                    viewModel.reserveRoom(
                        ReserveRequest(hotel.id, roomId, startDate, endDate, guestName, guestEmail)
                    )
                }
            }
        }

        if (reservations.isNotEmpty()) {
            SectionTitle("Tus reservas")
            reservations.forEach { reservation ->
                ReservationItem(reservation)
            }
        }
    }
}

@Composable
fun BackButton(onBack: () -> Unit) {
    Button(onClick = onBack) {
        Text("← Volver")
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SearchInputs(
    city: String, onCityChange: (String) -> Unit,
    startDate: String, onStartDateChange: (String) -> Unit,
    endDate: String, onEndDateChange: (String) -> Unit,
    roomId: String, onRoomIdChange: (String) -> Unit,
    guestName: String, onGuestNameChange: (String) -> Unit,
    guestEmail: String, onGuestEmailChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = startDate, onValueChange = onStartDateChange, label = { Text("Inicio (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = endDate, onValueChange = onEndDateChange, label = { Text("Fin (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = roomId, onValueChange = onRoomIdChange, label = { Text("ID de Habitación(R1, R2, R3)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = guestName, onValueChange = onGuestNameChange, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = guestEmail, onValueChange = onGuestEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ActionButtons(
    onFetchAll: () -> Unit,
    onShowReservations: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = onFetchAll) { Text("Ver hoteles") }
        OutlinedButton(onClick = onShowReservations) { Text("Mis reservas") }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SectionTitle(title: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = title, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun HotelItem(
    hotelName: String,
    rating: Int,
    address: String,
    imageUrl: String?,
    onReserveClick: () -> Unit
) {
    val baseUrl = "http://13.39.162.212"
    val finalImageUrl = imageUrl?.removePrefix("/")?.let { "$baseUrl/$it" }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {

        finalImageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Hotel Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 8.dp),
                placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                error = painterResource(android.R.drawable.ic_delete)
            )
        }

        Text("🏨 $hotelName ($rating⭐)")
        Text("📍 $address")
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = onReserveClick) {
            Text("Reservar")
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun ReservationItem(reservation: Reservation) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("🏨 ${reservation.hotel.name} (${reservation.hotel.rating}⭐)")
        Text("📍 ${reservation.hotel.address}")
        Text("🛏 ${reservation.room.room_type} - ${reservation.room.price}€")
        Text("📅 ${reservation.start_date} → ${reservation.end_date}")
        Text("🔒 ID: ${reservation.id}")
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}
