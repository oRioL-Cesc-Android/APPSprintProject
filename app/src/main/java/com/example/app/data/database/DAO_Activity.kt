package com.example.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import javax.inject.Inject
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO_Activity {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addActivity(activity: Activites_Entities)

    @Query("SELECT * FROM Activites_Entities WHERE travel_id = :travelId")
    fun getActivitiesForTravel(travelId: Int): Flow<List<Activites_Entities>>

    @Delete
    suspend fun deleteActivity(activity: Activites_Entities)

    @Query("""
    UPDATE Activites_Entities 
    SET nameActivity = :name, 
        ubicacion = :ubicacion, 
        duration = :duration 
    WHERE travel_id = :travelId 
    AND activity_id = :activityId
""")
    suspend fun updateActivity(
        travelId: Int,
        activityId: Int,  // Changed to Int
        name: String,
        ubicacion: String,
        duration: Int
    )}
