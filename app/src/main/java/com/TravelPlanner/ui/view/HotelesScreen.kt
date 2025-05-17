package com.TravelPlanner.ui.view

import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.TravelPlanner.R
import com.TravelPlanner.models.ReserveRequest
import com.TravelPlanner.models.Reservation
import com.TravelPlanner.models.Room
import com.TravelPlanner.ui.viewmodel.HotelViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.TravelPlanner.ui.view.DatePickerButton as DatePickerButton1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelesScreen(
    navController: NavController,
    viewModel: HotelViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var startDate by remember { mutableStateOf("2025-06-01") }
    var endDate by remember { mutableStateOf("2025-06-05") }
    var guestName by remember { mutableStateOf("John Doe") }
    var guestEmail by remember { mutableStateOf("john@example.com") }
    var showAllHotels by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("Barcelona") }
    var expanded by remember { mutableStateOf(false) }
    val cities = listOf(
        "Barcelona","Paris","Londres"
    )

    val hotels by viewModel.hotels.collectAsState()
    val allHotels by viewModel.allHotels.collectAsState()
    val filteredHotels = allHotels.filter { it.address.contains(selectedCity, ignoreCase = true) }
    val reservations = viewModel.userReservations
    val reservationResult = viewModel.reservationResult
    val reservationListStatus = viewModel.listReservationResult
    val reservationError = viewModel.reservationError

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.HotelesScreen)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Icon")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // DatePicker para fecha de inicio
            DatePickerButton1(
                label = stringResource(R.string.start_date),
                timestamp = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDate)?.time ?: System.currentTimeMillis(),
                onDateSelected = { newTimestamp ->
                    startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(newTimestamp))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // DatePicker para fecha final
            DatePickerButton1(
                label = stringResource(R.string.end_date),
                timestamp = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDate)?.time ?: System.currentTimeMillis(),
                onDateSelected = { newTimestamp ->
                    endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(newTimestamp))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = guestName,
                onValueChange = { guestName = it },
                label = { Text(stringResource(R.string.guest_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = guestEmail,
                onValueChange = { guestEmail = it },
                label = { Text(stringResource(R.string.guest_email)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.selecciona_ciudad)) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                selectedCity = city
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    viewModel.fetchAllHotels()
                    showAllHotels = true
                }) {
                    Text(stringResource(R.string.ver_hoteles))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            reservationError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            reservationResult?.let { success ->
                if (reservationError == null) {
                    Text(
                        text = if (success) stringResource(R.string.reserva_exitosa) else stringResource(R.string.reserva_fallida),
                        color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            reservationListStatus?.let {
                Text(
                    text = it,
                    color = if (it.startsWith("✅")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showAllHotels) {
                Text(
                    stringResource(R.string.hoteles_en, selectedCity),
                    style = MaterialTheme.typography.titleMedium
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 600.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    filteredHotels.forEach { hotel ->
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
                Text(stringResource(R.string.tus_reservas), style = MaterialTheme.typography.titleMedium)

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
