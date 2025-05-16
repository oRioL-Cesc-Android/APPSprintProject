package com.TravelPlanner.data.repo

import com.TravelPlanner.data.remote.api.HotelApiService
import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.ReserveRequest
import retrofit2.Response
import javax.inject.Inject

class HotelRepository @Inject constructor(
    private val api: HotelApiService
) {
    val groupId = "G06"
    suspend fun getHotels(): List<Hotel> =
        api.getHotels(groupId)

    suspend fun checkAvailability( startDate: String, endDate: String, city: String?): List<Hotel> =
        api.checkAvailability(groupId, startDate, endDate, city)

    suspend fun reserveRoom(request: ReserveRequest): Response<Unit> =
        api.reserveRoom(groupId, request)
}