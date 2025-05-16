package com.TravelPlanner.data.repo

import com.TravelPlanner.data.remote.dto.AvailabilityDto
import com.TravelPlanner.data.remote.dto.HotelDto
import com.TravelPlanner.data.remote.dto.ReservationDto
import com.TravelPlanner.data.remote.dto.ReservationResponseBody
import com.TravelPlanner.data.remote.dto.ResponseBody
import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.ReserveRequest
import com.zeni.core.data.remote.api.HotelApiService
import javax.inject.Inject
import javax.inject.Singleton



interface HotelRepository {

    /* ---------- Hotels & Availability ---------- */
    suspend fun getHotels(): List<Hotel>
    suspend fun getAvailability(
        groupId: String,
        start: String,
        end: String,
        hotelId: String? = null,
        city: String? = null
    ): List<Hotel>

    /* ---------- Make & cancel reservation (by group) ---------- */
    suspend fun reserve(groupId: String, request: ReserveRequest): ReservationDto
    suspend fun cancel(groupId: String, request: ReserveRequest): String   // returns message

    /* ---------- Reservations queries ---------- */
    suspend fun getGroupReservations(
        groupId: String,
        guestEmail: String? = null
    ): List<ReservationDto>

    suspend fun getAllReservations(): Map<String, List<ReservationDto>>

    /* ---------- Operations by reservation-id ---------- */
    suspend fun getReservationById(resId: String): ReservationDto
    suspend fun cancelById(resId: String): ReservationDto
}