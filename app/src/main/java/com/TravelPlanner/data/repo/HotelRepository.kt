package com.TravelPlanner.data.repo

import com.TravelPlanner.data.remote.api.HotelApiService
import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.ReserveRequest
import retrofit2.Response
import javax.inject.Inject
import com.TravelPlanner.models.Reservation

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
    suspend fun listReservation(guestEmail: String): List<Reservation> {
        return api.getReservations(groupId, guestEmail).reservations
    }
    suspend fun deleteReservation(reservationId: String): Boolean {
        return try {
            val response = api.deleteReservation(reservationId)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}