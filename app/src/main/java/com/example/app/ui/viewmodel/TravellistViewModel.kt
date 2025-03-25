package com.example.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.ui.screens.Activitys
import com.example.app.ui.screens.TravelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TravelListViewModel @Inject constructor() : ViewModel() {
    private val _travelItems = MutableStateFlow<List<TravelItem>>(emptyList())
    private val _activitys = MutableStateFlow<List<Activitys>>(emptyList())
    val travelItems: StateFlow<List<TravelItem>> = _travelItems
    val activitys: StateFlow<List<Activitys>> = _activitys
    fun addTravelItem(item: TravelItem) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value + item
        }
    }

    fun updateTravelItem(updatedItem: TravelItem) {
        viewModelScope.launch {
            _travelItems.value =
                _travelItems.value.map { if (it.id == updatedItem.id) updatedItem else it }
        }
    }

    fun deleteTravelItem(item: TravelItem) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value - item
        }
    }

    fun saveUpdatedTravelItem(updatedItem: TravelItem) {
        // Aquí actualizas el viaje con las actividades editadas
        _travelItems.value = _travelItems.value.map {
            if (it.id == updatedItem.id) updatedItem else it
        }
    }

    fun addActivityToTravel(travelId: Int, activity: Activitys) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value.map {
                if (it.id == travelId) it.copy(activities = it.activities + activity) else it
            }
        }
    }

    fun removeActivityFromTravel(travelId: Int, activity: Activitys) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value.map { travel ->
                if (travel.id == travelId) {
                    travel.copy(
                        activities = travel.activities.filter { it.nameActivity != activity.nameActivity }
                    )
                } else travel
            }
        }
    }


    fun updateActivityInTravel(travelId: Int, updatedActivity: Activitys) {
        viewModelScope.launch {
            _travelItems.value = _travelItems.value.map {
                if (it.id == travelId) {
                    it.copy(activities = it.activities.map { act ->
                        if (act.nameActivity == updatedActivity.nameActivity) updatedActivity else act
                    })
                } else it
            }
        }
    }

    // TravelListViewModel.kt

    private val _editingItemId = MutableStateFlow<Int?>(null)
    val editingItemId: StateFlow<Int?> = _editingItemId

    fun startEditing(id: Int) {
        _editingItemId.value = id
    }

    fun stopEditing() {
        val currentEditingItem = _travelItems.value.find { it.id == _editingItemId.value }

        // Si el item está vacío, lo eliminamos antes de salir de edición
        if (currentEditingItem != null && currentEditingItem.title.isBlank() &&
            currentEditingItem.location.isBlank() &&
            currentEditingItem.description.isBlank() &&
            currentEditingItem.rating == 0f &&
            currentEditingItem.duration.isBlank()) {

            _travelItems.value = _travelItems.value - currentEditingItem
        }

        _editingItemId.value = -1
    }


}