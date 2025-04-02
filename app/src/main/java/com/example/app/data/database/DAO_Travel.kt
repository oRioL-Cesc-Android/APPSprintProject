package com.example.app.data.database

import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

}

