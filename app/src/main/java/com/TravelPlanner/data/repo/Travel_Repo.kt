package com.TravelPlanner.data.repo

import com.TravelPlanner.data.database.DAO.DAO_Travel
import com.TravelPlanner.data.database.entities.Travel_Entities
import com.TravelPlanner.models.ActivityItems
import com.TravelPlanner.models.TravelItem

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class Travel_Repo @Inject constructor(
    private val Travel_DAO: DAO_Travel
) {
    suspend fun addTravelItem(item: TravelItem) {
        Travel_DAO.addTravelItem(
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

    suspend fun updateTravelItem(item: TravelItem) {
        Travel_DAO.updateTravelItem(
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

    suspend fun deleteTravelItem(item: TravelItem) {
        Travel_DAO.deleteTravelItem(
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

    suspend fun getTravelById(id: Int): Travel_Entities? {
        return Travel_DAO.getTravelById(id)
    }

    fun getAllTravels(): Flow<List<TravelItem>> {
        return Travel_DAO.ObtenerTravel().map { travelList ->
            travelList.map { travel ->
                TravelItem(
                    id = travel.travel.id,
                    title = travel.travel.title,
                    location = travel.travel.location,
                    description = travel.travel.description,
                    rating = travel.travel.rating,
                    fechainicio = travel.travel.fechainicio,
                    fechafinal = travel.travel.fechafinal,

                    activities = travel.activities.map {
                        ActivityItems(
                            activity_id = it.activity_id,  // Pass the activity_id
                            nameActivity = it.nameActivity,
                            ubicacion = it.ubicacion,
                            duration = it.duration
                        )
                    }
                )
            }
        }

    }
}
