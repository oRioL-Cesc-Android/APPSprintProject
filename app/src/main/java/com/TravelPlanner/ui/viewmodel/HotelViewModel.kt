package com.TravelPlanner.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TravelPlanner.data.repo.HotelRepository
import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.Reservation
import com.TravelPlanner.models.ReserveRequest
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
            reservationResult = try {
                repository.reserveRoom(request).isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }

    fun listReservation(guestEmail: String) {
        viewModelScope.launch {
            try {
                userReservations = repository.listReservation(guestEmail)
                listReservationResult = "✅ Reservas cargadas"
            } catch (e: Exception) {
                userReservations = emptyList()
                listReservationResult = "❌ Error: ${e.localizedMessage}"
            }
        }
    }

}
