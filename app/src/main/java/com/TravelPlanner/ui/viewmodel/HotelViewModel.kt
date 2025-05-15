package com.TravelPlanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TravelPlanner.data.remote.dto.AvailabilityDto
import com.TravelPlanner.data.repo.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val repository: HotelRepository
) : ViewModel() {

    private val _availability = MutableStateFlow<List<AvailabilityDto>>(emptyList())
    val availability: StateFlow<List<AvailabilityDto>> = _availability

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchAvailability(
        groupId: String,
        startDate: String,
        endDate: String,
        city: String
    ) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.checkAvailability(groupId, startDate, endDate, city = city)
                _availability.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
