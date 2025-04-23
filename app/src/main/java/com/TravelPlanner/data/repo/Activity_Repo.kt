package com.TravelPlanner.data.repo

import com.TravelPlanner.data.database.DAO.DAO_Activity
import com.TravelPlanner.data.database.entities.Activites_Entities


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Activity_Repo @Inject constructor(
    private val Activity_DAO: DAO_Activity
) {

    suspend fun addOrUpdateActivity(
        travelId: Int,
        activity: Activites_Entities
    ) {
        Activity_DAO.updateActivity(activity.copy(travel_id = travelId))
    }

    suspend fun deleteActivity(
        travelId: Int,
        activityId: Int
    ) {
        Activity_DAO.deleteActivity(
            Activites_Entities(
                travel_id = travelId,
                activity_id = activityId,
                nameActivity = "",
                ubicacion = "",
                duration = 0
            )
        )
    }

    suspend fun updateActivity(
        travelId: Int,
        updatedActivity: Activites_Entities
    ) {
        Activity_DAO.updateActivity(updatedActivity.copy(travel_id = travelId))
    }


}
