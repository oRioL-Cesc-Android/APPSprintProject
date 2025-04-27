package com.TravelPlanner.data.database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.TravelPlanner.data.database.entities.TravelRelation
import com.TravelPlanner.data.database.entities.Travel_Entities
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO_Travel {
    @Upsert
    suspend fun addTravelItem(travelEntities: Travel_Entities)

    @Query("SELECT * FROM travel_table")
    fun ObtenerTravel(): Flow<List<TravelRelation>>

    @Delete
    suspend fun deleteTravelItem(travelEntities: Travel_Entities)

    @Upsert
    suspend fun updateTravelItem(travelEntities: Travel_Entities)

    @Query("SELECT * FROM travel_table WHERE id = :travelId LIMIT 1")
    suspend fun getTravelById(travelId: Int): Travel_Entities?

    @Insert
    suspend fun insertTravel(travel: Travel_Entities)

    @Query("SELECT * FROM travel_table WHERE user_owner = :username")
    suspend fun getTravelsForUser(username: String): List<Travel_Entities>

}

