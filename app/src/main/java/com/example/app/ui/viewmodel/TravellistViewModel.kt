package com.example.app.ui.viewmodel

import android.R.attr.description
import android.R.attr.duration
import android.R.attr.rating
import android.util.Log
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
import java.time.LocalDateTime
import java.time.ZoneOffset
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
//            val activities = activityDAO.getActivitiesForTravel(travel.id).firstOrNull()?.map {
//                Activitys(
//                    activity_id = it.activity_id,  // Pass the activity_id
//                    nameActivity = it.nameActivity,
//                    ubicacion = it.ubicacion,
//                    duration = it.duration
//                )
//            } ?: emptyList()
            TravelItem(
                id = travel.travel.id,
                title = travel.travel.title,
                location = travel.travel.location,
                description = travel.travel.description,
                rating = travel.travel.rating,
                fechainicio = travel.travel.fechainicio,
                fechafinal = travel.travel.fechafinal,

                activities = travel.activities.map {
                    Activitys(
                        activity_id = it.activity_id,  // Pass the activity_id
                        nameActivity = it.nameActivity,
                        ubicacion = it.ubicacion,
                        duration = it.duration
                    )
                }
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
                    fechainicio = item.fechainicio,
                    fechafinal = item.fechafinal
                )
            )
        }
    }

    fun updateTravelItem(updatedItem: TravelItem) {
        viewModelScope.launch {
            DAO.updateTravelItem(
                Travel_Entities(
                    updatedItem.id,
                    updatedItem.title,
                    updatedItem.location,
                    updatedItem.description,
                    updatedItem.rating,
                    updatedItem.fechainicio,
                    updatedItem.fechafinal
                    )
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
                    fechainicio = item.fechainicio,
                    fechafinal = item.fechafinal
                )
            )
        }
    }

    fun addActivityToTravel(travelId: Int, activity: Activitys) {
        viewModelScope.launch {
            activityDAO.updateActivity(
                Activites_Entities(
                    travel_id = travelId,
                    activity_id = activity.activity_id,  // Now using Int
                    nameActivity = activity.nameActivity,
                    ubicacion = activity.ubicacion,
                    duration = activity.duration
                )
            )
        }
    }




    /*fun removeActivityFromTravel(travelId: Int, activity: Activitys) {
        viewModelScope.launch {
            activityDAO.deleteActivity(
                Activites_Entities(
                    travel_id = travelId,
                    activity_id = activity.activity_id,
                    nameActivity = activity.nameActivity,
                    ubicacion = activity.ubicacion,
                    duration = activity.duration
                )
            )
        }
    }*/

    fun removeActivityFromTravel(travelId: Int, activity: Activitys) {
        viewModelScope.launch {
            Log.d("ViewModel", "Llamando a eliminar actividad con ID: ${activity.activity_id}")
            activityDAO.deleteActivity(
                Activites_Entities(
                    travel_id = travelId,
                    activity_id = activity.activity_id,
                    nameActivity = "",
                    ubicacion = "",
                    duration = 0
                ))
        }
    }


    fun updateTravel( updatedTravel: TravelItem){
        viewModelScope.launch {
            DAO.updateTravelItem(
                Travel_Entities(
                    id = updatedTravel.id,
                    title = updatedTravel.title,
                    location = updatedTravel.location,
                    description = updatedTravel.description,
                    rating = updatedTravel.rating,
                    fechainicio = updatedTravel.fechainicio,
                    fechafinal = updatedTravel.fechafinal
                )
            )
        }
    }

    fun updateActivityInTravel(travelId: Int, updatedActivity: Activitys) {
        viewModelScope.launch {
            activityDAO.updateActivity(
                Activites_Entities(
                    travel_id = travelId,
                    activity_id = updatedActivity.activity_id,  // Now using Int
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
            val currentEditingItem = DAO.getTravelById(_editingItemId.value ?: return@launch)
            if (currentEditingItem != null && currentEditingItem.title.isBlank() &&
                currentEditingItem.location.isBlank() &&
                currentEditingItem.description.isBlank() &&
                currentEditingItem.rating == 0f &&
                currentEditingItem.fechainicio < LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) &&
                currentEditingItem.fechafinal < currentEditingItem.fechainicio
            ) {
                DAO.deleteTravelItem(currentEditingItem)
            }
            _editingItemId.value = null
        }
    }
}
