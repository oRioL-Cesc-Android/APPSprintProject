package com.TravelPlanner.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TravelPlanner.data.repo.HotelRepository
import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.Reservation
import com.TravelPlanner.models.ReserveRequest
import com.TravelPlanner.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val repository: HotelRepository
) : ViewModel() {
    var userReservations by mutableStateOf<List<Reservation>>(emptyList())
        private set

    var listReservationResult by mutableStateOf<String?>(null)
        private set

    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())
    val hotels: StateFlow<List<Hotel>> = _hotels

    private val _allHotels = MutableStateFlow<List<Hotel>>(emptyList())
    val allHotels: StateFlow<List<Hotel>> = _allHotels

    var reservationResult by mutableStateOf<Boolean?>(null)
        private set

    var reservationError by mutableStateOf<String?>(null)
        private set

    var deleteReservationResult by mutableStateOf<String?>(null)
        private set

    fun fetchAllHotels() {
        viewModelScope.launch {
            try {
                _allHotels.value = repository.getHotels()
            } catch (e: Exception) {
                _allHotels.value = emptyList()
            }
        }
    }

    fun searchHotels(startDate: String, endDate: String, city: String?) {
        viewModelScope.launch {
            try {
                _hotels.value = repository.checkAvailability(startDate, endDate, city)
            } catch (e: Exception) {
                _hotels.value = emptyList()
            }
        }
    }

    fun reserveRoom(request: ReserveRequest) {
        viewModelScope.launch {
            // Validar que la fecha de inicio no sea después de la fecha final
            if (request.start_date > request.end_date) {
                reservationResult = false
                reservationError = "❌ No se puede reservar una habitación si la fecha de inicio es después que la fecha final."
                return@launch
            }
            reservationError = null
            reservationResult = try {
                repository.reserveRoom(request).isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }

    fun listReservation(guestEmail: String, context: Context) {
        viewModelScope.launch {
            try {
                userReservations = repository.listReservation(guestEmail)
                listReservationResult = context.getString(R.string.reservas_cargadas)
            } catch (e: Exception) {
                userReservations = emptyList()
                listReservationResult = context.getString(R.string.error_cargar_reservas, e.localizedMessage ?: "")
            }
        }
    }

    fun deleteReservation(reservationId: String, context: Context) {
        viewModelScope.launch {
            deleteReservationResult = try {
                if (repository.deleteReservation(reservationId)) {
                    userReservations = userReservations.filter { it.id != reservationId }
                    context.getString(R.string.reserva_eliminada_correctamente)
                } else {
                    context.getString(R.string.error_eliminar_reserva)
                }
            } catch (e: Exception) {
                context.getString(R.string.error_eliminar_reserva_con_mensaje, e.localizedMessage ?: "")
            }
        }
    }

    fun clearDeleteReservationResult() {
        deleteReservationResult = null
    }
}
