package com.example.app.data.database

import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.app.ui.screens.TravelItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Dao
interface DAO_Travel {
    @Upsert
    suspend fun addTravelItem(travelEntities: Travel_Entities)

    @Query("SELECT * FROM TRAVEL_ENTITIES")
    fun ObtenerTravel(): Flow<List<Travel_Entities>>

    @Delete
    suspend fun deleteTravelItem(travelEntities: Travel_Entities)

    @Query("UPDATE Travel_Entities SET title = :title, location = :location, description = :description, rating = :rating, duration = :duration WHERE id = :id")
    suspend fun updateTravelItem(id: Int, title: String, location: String, description: String, rating: Float, duration: String)

    @Query("SELECT * FROM TRAVEL_ENTITIES WHERE id = :travelId LIMIT 1")
    suspend fun getTravelById(travelId: Int): Travel_Entities?

}

