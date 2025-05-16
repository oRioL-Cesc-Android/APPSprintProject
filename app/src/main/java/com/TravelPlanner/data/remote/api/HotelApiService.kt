package com.zeni.core.data.remote.api


import com.TravelPlanner.data.remote.dto.AvailabilityDto
import com.TravelPlanner.data.remote.dto.HotelDto
import com.TravelPlanner.data.remote.dto.ReservationDto
import com.TravelPlanner.data.remote.dto.ReservationResponseBody
import com.TravelPlanner.data.remote.dto.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.DELETE

interface HotelApiService {

    /* ------------ Hotels & Availability ------------ */

    @GET("hotels/{group_id}/hotels")
    suspend fun getHotels(
        @Path("group_id") groupId: String
    ): List<HotelDto>

    @GET("hotels/{group_id}/availability")
    suspend fun getAvailability(
        @Path("group_id") groupId: String,
        @Query("start_date") startDate: String,
        @Query("end_date")   endDate: String,
        @Query("hotel_id")   hotelId: String? = null,
        @Query("city")   city: String? = null
    ): AvailabilityDto

    /* ------------ Reservations by group ------------ */

    @POST("hotels/{group_id}/reserve")
    suspend fun reserveRoom(
        @Path("group_id") groupId: String,
        @Body             request: ReservationDto
    ): ReservationResponseBody

    @POST("hotels/{group_id}/cancel")
    suspend fun cancelReservation(
        @Path("group_id") groupId: String,
        @Body             request: CancelRequestDto            // same fields as Reserve
    ): ApiMessageDto                                           // e.g. { "message": "Reserva cancelada" }

    @GET("hotels/{group_id}/reservations")
    suspend fun getGroupReservations(
        @Path("group_id") groupId: String,
        @Query("guest_email") guestEmail: String? = null
    ): ReservationsWrapperDto                                  // { reservations:[...] }

    /* ------------ Admin-level (all groups) ------------ */

    @GET("reservations")
    suspend fun getAllReservations(): AllReservationsDto       // { groups:{ G01:[...], G02:[...] } }

    @GET("reservations/{res_id}")
    suspend fun getReservationById(
        @Path("res_id") resId: String
    ): ReservationDto

    @DELETE("reservations/{res_id}")
    suspend fun deleteReservationById(
        @Path("res_id") resId: String
    ): ReservationDto                                          // returns the deleted object
}

/* Cancel uses the same body as Reserve */
typealias CancelRequestDto = ReservationDto

/* Generic message wrapper */
data class ApiMessageDto(val message: String)

/* Wrapper used by  GET /hotels/{group}/reservations */
data class ReservationsWrapperDto(
    val reservations: List<ReservationDto>
)

/* Wrapper used by  GET /reservations */
data class AllReservationsDto(
    val groups: Map<String, List<ReservationDto>>
)