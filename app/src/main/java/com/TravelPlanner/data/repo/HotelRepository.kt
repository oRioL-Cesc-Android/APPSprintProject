package com.TravelPlanner.data.repo

import com.TravelPlanner.data.remote.api.HotelApiService
import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.ReserveRequest
import retrofit2.Response
import javax.inject.Inject

class HotelRepository @Inject constructor(
    private val api: HotelApiService
) {

    suspend fun getHotels(groupId: String): List<Hotel> =
        api.getHotels(groupId)

    suspend fun checkAvailability(groupId: String, startDate: String, endDate: String, city: String?): List<Hotel> =
        api.checkAvailability(groupId, startDate, endDate, city)

    suspend fun reserveRoom(groupId: String, request: ReserveRequest): Response<Unit> =
        api.reserveRoom(groupId, request)
}