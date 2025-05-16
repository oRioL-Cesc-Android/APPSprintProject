package com.TravelPlanner.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TravelPlanner.data.remote.dto.*
import com.TravelPlanner.data.repo.HotelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class HotelViewModel(
    private val repository: HotelRepository
) : ViewModel() {

    private val _hotels = MutableStateFlow<UiState<List<HotelDto>>>(UiState.Loading)
    val hotels: StateFlow<UiState<List<HotelDto>>> = _hotels.asStateFlow()

    private val _availability = MutableStateFlow<UiState<AvailabilityDto>>(UiState.Loading)
    val availability: StateFlow<UiState<AvailabilityDto>> = _availability.asStateFlow()

    private val _reservations = MutableStateFlow<UiState<List<ReservationDto>>>(UiState.Loading)
    val reservations: StateFlow<UiState<List<ReservationDto>>> = _reservations.asStateFlow()

    fun fetchHotels(groupId: String) {
        viewModelScope.launch {
            _hotels.value = UiState.Loading
            try {
                val hotelList = repository.getHotels(groupId)
                _hotels.value = UiState.Success(hotelList)
            } catch (e: Exception) {
                _hotels.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun checkAvailability(
        groupId: String,
        startDate: String,
        endDate: String,
        hotelId: String? = null,
        city: String? = null
    ) {
        viewModelScope.launch {
            _availability.value = UiState.Loading
            try {
                val result = repository.getHotelAvailability(groupId, startDate, endDate, hotelId, city)
                _availability.value = UiState.Success(result)
            } catch (e: Exception) {
                _availability.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchReservations(groupId: String, guestEmail: String? = null) {
        viewModelScope.launch {
            _reservations.value = UiState.Loading
            try {
                val result = repository.getReservations(groupId, guestEmail)
                _reservations.value = UiState.Success(result)
            } catch (e: Exception) {
                _reservations.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun reserveHotel(groupId: String, reservation: ReservationDto, onResult: (Result<ReservationResponseBody>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.reserveHotel(groupId, reservation)
                onResult(Result.success(response))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    fun cancelReservation(groupId: String, reservation: ReservationDto, onResult: (Result<ResponseBody>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.cancelReservation(groupId, reservation)
                onResult(Result.success(response))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }
}
