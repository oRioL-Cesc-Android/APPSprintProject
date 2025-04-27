package com.TravelPlanner.data.database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.TravelPlanner.data.database.entities.TravelRelation
import com.TravelPlanner.data.database.entities.Travel_Entities
import com.TravelPlanner.data.database.entities.UserWithTravels
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO_Travel {
    @Upsert
    suspend fun addTravelItem(travelEntities: Travel_Entities)

    @Query("SELECT * FROM TRAVEL_ENTITIES")
    fun ObtenerTravel(): Flow<List<TravelRelation>>

    @Delete
    suspend fun deleteTravelItem(travelEntities: Travel_Entities)

    @Upsert
    suspend fun updateTravelItem(travelEntities: Travel_Entities)

    @Query("SELECT * FROM TRAVEL_ENTITIES WHERE id = :travelId LIMIT 1")
    suspend fun getTravelById(travelId: Int): Travel_Entities?

    @Transaction
    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getUserWithTravels(username: String): Flow<UserWithTravels>

}

