package com.TravelPlanner.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.TravelPlanner.ui.viewmodel.HotelViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(
    navController: NavHostController,
    viewModel: HotelViewModel = hiltViewModel()
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val userEmail = currentUser?.email

    var guestEmail by remember { mutableStateOf("") }
    val reservations = viewModel.userReservations
    val reservationListStatus = viewModel.listReservationResult
    val deleteReservationResult = viewModel.deleteReservationResult
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Solo llamamos a listReservation si el usuario está autenticado
    LaunchedEffect(userEmail) {
        userEmail?.let {
            guestEmail = it
            viewModel.listReservation(it, context)
        }
    }

    // Mostrar el mensaje de eliminación como un Snackbar
    LaunchedEffect(deleteReservationResult) {
        deleteReservationResult?.let { result ->
            snackbarHostState.showSnackbar(result)
            viewModel.clearDeleteReservationResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Icon"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (userEmail == null) {
                Text(
                    text = "Por favor, inicia sesión para ver tus reservas.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            } else if (reservations.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Tus Reservas:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    reservations.forEach { reservation ->
                        Box(modifier = Modifier.padding(vertical = 8.dp)) {
                            IconButton(
                                onClick = { viewModel.deleteReservation(reservation.id, context) },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar reserva",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                            ReservationItem(reservation = reservation)
                        }
                    }
                }
            } else {
                reservationListStatus?.let {
                    Text(
                        text = it,
                        color = if (it.startsWith("✅")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } ?: Text(
                    text = "No tienes reservas realizadas.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
