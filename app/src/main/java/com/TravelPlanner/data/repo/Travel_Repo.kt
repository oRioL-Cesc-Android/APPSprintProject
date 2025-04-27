package com.TravelPlanner.data.repository

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
    private val travelDao: DAO_Travel
) {

    suspend fun addTravelItem(item: TravelItem) {
        val travelEntity = item.toEntity()
        travelDao.addTravelItem(travelEntity)
    }

    suspend fun updateTravelItem(item: TravelItem) {
        val travelEntity = item.toEntity()
        travelDao.updateTravelItem(travelEntity)
    }

    suspend fun deleteTravelItem(item: TravelItem) {
        val travelEntity = item.toEntity()
        travelDao.deleteTravelItem(travelEntity)
    }

    suspend fun getTravelById(id: Int): Travel_Entities? {
        return travelDao.getTravelById(id)
    }

    fun getAllTravels(): Flow<List<TravelItem>> {
        return travelDao.ObtenerTravel().map { travelList ->
            travelList.map { travelWithActivities ->
                travelWithActivities.toTravelItem()
            }
        }
    }
}

// Extension functions para mapear fácilmente
private fun TravelItem.toEntity(): Travel_Entities {
    return Travel_Entities(
        id = id,
        title = title,
        location = location,
        description = description,
        valoracion = rating,
        fechainicio = fechainicio,
        fechafinal = fechafinal,
        userOwner = usuario
    )
}

private fun com.TravelPlanner.data.database.entities.TravelRelation.toTravelItem(): TravelItem {
    return TravelItem(
        id = travel.id,
        title = travel.title,
        location = travel.location,
        description = travel.description,
        rating = travel.valoracion,
        fechainicio = travel.fechainicio,
        fechafinal = travel.fechafinal,
        usuario = travel.userOwner,
        activities = activities.map {
            ActivityItems(
                activity_id = it.activity_id,
                nameActivity = it.nameActivity,
                ubicacion = it.ubicacion,
                duration = it.duration
            )
        }
    )
}
