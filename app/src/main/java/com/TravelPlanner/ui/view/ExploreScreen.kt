package com.TravelPlanner.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.TravelPlanner.models.ReserveRequest
import com.TravelPlanner.models.Reservation
import com.TravelPlanner.models.Room
import com.TravelPlanner.ui.viewmodel.HotelViewModel

@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: HotelViewModel = hiltViewModel()
) {
    var startDate by remember { mutableStateOf("2025-06-01") }
    var endDate by remember { mutableStateOf("2025-06-05") }
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
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = guestName,
            onValueChange = { guestName = it },
            label = { Text("Guest Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = guestEmail,
            onValueChange = { guestEmail = it },
            label = { Text("Guest Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                viewModel.fetchAllHotels()
                showAllHotels = true
            }) {
                Text("Ver Hoteles")
            }

            Button(onClick = {
                viewModel.listReservation(guestEmail)
                showAllHotels = false
            }) {
                Text("Mis Reservas")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        reservationResult?.let { success ->
            Text(
                text = if (success) "✅ Reservation successful!" else "❌ Reservation failed.",
                color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        reservationListStatus?.let {
            Text(
                text = it,
                color = if (it.startsWith("✅")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (showAllHotels) {
            Text("Todos los Hoteles:", style = MaterialTheme.typography.titleMedium)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                allHotels.forEach { hotel ->
                    HotelItemWithRooms(
                        hotelName = hotel.name,
                        rating = hotel.rating,
                        address = hotel.address,
                        hotelImageUrl = hotel.image_url,
                        rooms = hotel.rooms,
                        startDate = startDate,
                        endDate = endDate,
                        guestName = guestName,
                        guestEmail = guestEmail,
                        onReserveClick = { roomId ->
                            val reserveRequest = ReserveRequest(
                                hotel_id = hotel.id,
                                room_id = roomId,
                                start_date = startDate,
                                end_date = endDate,
                                guest_name = guestName,
                                guest_email = guestEmail
                            )
                            viewModel.reserveRoom(reserveRequest)
                        }
                    )
                }
            }
        }

        if (reservations.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Tus Reservas:", style = MaterialTheme.typography.titleMedium)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                reservations.forEach { reservation ->
                    ReservationItem(reservation = reservation)
                }
            }
        }
    }
}

@Composable
fun HotelItemWithRooms(
    hotelName: String,
    rating: Int,
    address: String,
    hotelImageUrl: String?,
    rooms: List<Room>,
    startDate: String,
    endDate: String,
    guestName: String,
    guestEmail: String,
    onReserveClick: (roomId: String) -> Unit
) {
    val baseUrl = "http://13.39.162.212"

    val finalHotelImageUrl = hotelImageUrl?.removePrefix("/")?.let { "$baseUrl/$it" }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {

        finalHotelImageUrl?.let {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .crossfade(true)
                    .build(),
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

        rooms.forEach { room ->
            Spacer(modifier = Modifier.height(8.dp))
            Text("🛏 ${room.room_type} - ${room.price}€")

            // Construir la URL completa para la imagen de la habitación
            val roomImageUrl = room.image_url?.let { "$baseUrl/images/$it" }

            roomImageUrl?.let { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Room Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 8.dp),
                    placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                    error = painterResource(android.R.drawable.ic_delete)
                )
            }

            Button(onClick = { onReserveClick(room.id) }) {
                Text("Reservar esta habitación")
            }
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
