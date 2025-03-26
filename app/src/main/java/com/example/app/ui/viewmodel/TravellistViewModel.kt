package com.example.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.database.DAO_Travel
import com.example.app.data.database.Travel_Entities
import com.example.app.data.database.DAO_Activity
import com.example.app.data.database.Activites_Entities
import com.example.app.ui.screens.Activitys
import com.example.app.ui.screens.TravelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelListViewModel @Inject constructor(
    private val DAO: DAO_Travel,
    private val activityDAO: DAO_Activity
) : ViewModel() {
    private val _editingItemId = MutableStateFlow<Int?>(null)
    val editingItemId: StateFlow<Int?> = _editingItemId

    val travelItems: StateFlow<List<TravelItem>> = DAO.ObtenerTravel().map { travelList ->
        travelList.map { travel ->
            val activities = activityDAO.getActivitiesForTravel(travel.id).firstOrNull()?.map {
                Activitys(
                    activity_id = it.activity_id,  // Pass the activity_id
                    nameActivity = it.nameActivity,
                    ubicacion = it.ubicacion,
                    duration = it.duration
                )
            } ?: emptyList()
            TravelItem(
                id = travel.id,
                title = travel.title,
                location = travel.location,
                description = travel.description,
                rating = travel.rating,
                duration = travel.duration,
                activities = activities
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addTravelItem(item: TravelItem) {
        viewModelScope.launch {
            DAO.addTravelItem(
                Travel_Entities(
                    id = item.id,
                    title = item.title,
                    location = item.location,
                    description = item.description,
                    rating = item.rating,
                    duration = item.duration
                )
            )
        }
    }

    fun updateTravelItem(updatedItem: TravelItem) {
        viewModelScope.launch {
            DAO.updateTravelItem(
                updatedItem.id,
                updatedItem.title,
                updatedItem.location,
                updatedItem.description,
                updatedItem.rating,
                updatedItem.duration
            )
        }
    }

    fun deleteTravelItem(item: TravelItem) {
        viewModelScope.launch {
            DAO.deleteTravelItem(
                Travel_Entities(
                    id = item.id,
                    title = item.title,
                    location = item.location,
                    description = item.description,
                    rating = item.rating,
                    duration = item.duration
                )
            )
        }
    }

    fun addActivityToTravel(travelId: Int, activity: Activitys) {
        viewModelScope.launch {
            activityDAO.addActivity(
                Activites_Entities(
                    travel_id = travelId,
                    nameActivity = activity.nameActivity,
                    ubicacion = activity.ubicacion,
                    duration = activity.duration
                )
            )
        }
    }

    fun removeActivityFromTravel(travelId: Int, activity: Activitys) {
        viewModelScope.launch {
            activityDAO.deleteActivity(
                Activites_Entities(
                    travel_id = travelId,
                    nameActivity = activity.nameActivity,
                    ubicacion = activity.ubicacion,
                    duration = activity.duration
                )
            )
        }
    }

    fun updateActivityInTravel(travelId: Int, updatedActivity: Activitys) {
        viewModelScope.launch {
            activityDAO.updateActivity(
                travelId = travelId,
                activityId = updatedActivity.activity_id,  // Now using Int
                name = updatedActivity.nameActivity,
                ubicacion = updatedActivity.ubicacion,
                duration = updatedActivity.duration
            )
        }
    }

    fun startEditing(id: Int) {
        _editingItemId.value = id
    }

    fun stopEditing() {
        viewModelScope.launch {
            val currentEditingItem = DAO.getTravelById(_editingItemId.value ?: return@launch)
            if (currentEditingItem != null && currentEditingItem.title.isBlank() &&
                currentEditingItem.location.isBlank() &&
                currentEditingItem.description.isBlank() &&
                currentEditingItem.rating == 0f &&
                currentEditingItem.duration.isBlank()
            ) {
                DAO.deleteTravelItem(currentEditingItem)
            }
            _editingItemId.value = null
        }
    }
}
