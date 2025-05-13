package com.TravelPlanner.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TravelPlanner.data.repo.Travel_Repo
import com.TravelPlanner.data.repo.Activity_Repo
import com.TravelPlanner.models.ActivityItems
import com.TravelPlanner.models.TravelItem
import com.TravelPlanner.data.database.entities.Activites_Entities
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class TravelListViewModel @Inject constructor(
    private val travelRepo: Travel_Repo,
    private val activityRepo: Activity_Repo
) : ViewModel() {

    private val _editingItemId = MutableStateFlow<Int?>(null)
    val editingItemId: StateFlow<Int?> = _editingItemId

    val travelItems: StateFlow<List<TravelItem>> = travelRepo.getAllTravels().stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    fun addTravelItem(item: TravelItem) {
        viewModelScope.launch {
            travelRepo.addTravelItem(item)
        }
    }

    fun updateTravelItem(updatedItem: TravelItem) {
        viewModelScope.launch {
            travelRepo.updateTravelItem(updatedItem)
        }
    }

    fun deleteTravelItem(item: TravelItem) {
        viewModelScope.launch {
            travelRepo.deleteTravelItem(item)
        }
    }

    fun addActivityToTravel(travelId: Int, activity: ActivityItems) {
        viewModelScope.launch {
            activityRepo.addOrUpdateActivity(
                travelId,
                Activites_Entities(
                    travel_id = travelId,
                    activity_id = activity.activity_id,
                    nameActivity = activity.nameActivity,
                    ubicacion = activity.ubicacion,
                    duration = activity.duration
                )
            )
        }
    }

    fun removeActivityFromTravel(travelId: Int, activity: ActivityItems) {
        viewModelScope.launch {
            Log.d("ViewModel", "Llamando a eliminar actividad con ID: ${activity.activity_id}")
            activityRepo.deleteActivity(travelId, activity.activity_id)
        }
    }

    fun updateTravel(updatedTravel: TravelItem) {
        viewModelScope.launch {
            travelRepo.updateTravelItem(updatedTravel)
        }
    }

    fun updateActivityInTravel(travelId: Int, updatedActivity: ActivityItems) {
        viewModelScope.launch {
            activityRepo.updateActivity(
                travelId,
                Activites_Entities(
                    travel_id = travelId,
                    activity_id = updatedActivity.activity_id,
                    nameActivity = updatedActivity.nameActivity,
                    ubicacion = updatedActivity.ubicacion,
                    duration = updatedActivity.duration
                )
            )
        }
    }

    fun startEditing(id: Int) {
        _editingItemId.value = id
    }

    fun stopEditing() {
        viewModelScope.launch {
            val id = _editingItemId.value ?: return@launch
            val currentItem = travelRepo.getTravelById(id) ?: return@launch
            // Si el viaje es nuevo y está vacío, bórralo
            if (currentItem.title.isBlank() &&
                currentItem.location.isBlank() &&
                currentItem.description.isBlank() &&
                currentItem.rating == 0f &&
                (currentItem.imagePaths == null || currentItem.imagePaths.isEmpty())
            ) {
                travelRepo.deleteTravelItem(
                    TravelItem(
                        id = currentItem.id,
                        title = currentItem.title,
                        location = currentItem.location,
                        description = currentItem.description,
                        rating = currentItem.rating,
                        fechainicio = currentItem.fechainicio,
                        fechafinal = currentItem.fechafinal,
                        userName = currentItem.userName,
                        activities = emptyList(),
                        imagePaths = currentItem.imagePaths ?: emptyList()
                    )
                )
            }
            _editingItemId.value = null
        }
    }
}
