package com.example.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.ui.screens.TravelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TravelListViewModel @Inject constructor() : ViewModel() {
    private val _travelItems = MutableStateFlow<List<TravelItem>>(emptyList())
    val travelItems: StateFlow<List<TravelItem>> = _travelItems

    fun addTravelItem(item: TravelItem) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value + item
        }
    }

    fun updateTravelItem(updatedItem: TravelItem) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value.map { if (it.id == updatedItem.id) updatedItem else it }
        }
    }

    fun deleteTravelItem(item: TravelItem) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value - item
        }
    }

    fun saveUpdatedTravelItem(updatedItem: TravelItem) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value.map {
                if (it.id == updatedItem.id) updatedItem.copy(isEditing = false) else it
            }
        }
    }
}