package com.TravelPlanner.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TravelPlanner.data.remote.dto.*
import com.TravelPlanner.data.repo.HotelRepository
import com.TravelPlanner.models.Hotel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val repository: HotelRepository
) : ViewModel() {

    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())
    val hotels: StateFlow<List<Hotel>> = _hotels

    fun searchHotels(groupId: String, startDate: String, endDate: String, city: String?) {
        viewModelScope.launch {
            try {
                _hotels.value = repository.checkAvailability(groupId, startDate, endDate, city)
            } catch (e: Exception) {
                _hotels.value = emptyList() // o mostrar error
            }
        }
    }
}