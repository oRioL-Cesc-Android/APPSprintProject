package com.example.app.data.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import javax.inject.Inject
import androidx.room.Delete
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO_Activity {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addActivity(activity: Activites_Entities): Long




    @Query("SELECT * FROM Activites_Entities WHERE travel_id = :travelId")
    fun getActivitiesForTravel(travelId: Int): Flow<List<Activites_Entities>>



    @Delete
    suspend fun deleteActivity(activity: Activites_Entities) {
        Log.d("DAO_Activity", "Intentando borrar actividad con ID: ${activity.activity_id}")
    }
    //@Query("DELETE FROM Activites_Entities WHERE activity_id = :id")
    //suspend fun deleteActivity(id: Int)





    @Upsert
    suspend fun updateActivity(activity: Activites_Entities)
}
