package com.TravelPlanner.data.remote.api

import retrofit2.http.Query
import com.TravelPlanner.data.remote.dto.AvailabilityDto
import com.TravelPlanner.data.remote.dto.CancelRequestDto
import com.TravelPlanner.data.remote.dto.HotelDto
import com.TravelPlanner.data.remote.dto.ReservationAvailabilityDto
import com.TravelPlanner.data.remote.dto.ReservationDto
import com.TravelPlanner.data.remote.dto.ResponseBodyDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface HotelAPiservice {

    // HOTELS endpoints
    @GET("hotels/{group_id}/hotels")
    suspend fun listHotels(
        @Path("group_id") groupId: String
    ): List<HotelDto>

    @POST("hotels/{group_id}/reserve")
    suspend fun reserveRoom(
        @Path("group_id") groupId: String,
        @Body reservation: ReservationAvailabilityDto
    ): ResponseBodyDto

    @GET("hotels/{group_id}/availability")
    suspend fun checkAvailability(
        @Path("group_id") groupId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("hotel_id") hotelId: String? = null,
        @Query("city") city: String? = null
    ): List<AvailabilityDto>

    @POST("hotels/{group_id}/cancel")
    suspend fun cancelReservation(
        @Path("group_id") groupId: String,
        @Body cancelRequest: CancelRequestDto
    ): ResponseBodyDto

    @GET("hotels/{group_id}/reservations")
    suspend fun listGroupReservations(
        @Path("group_id") groupId: String
    ): List<ReservationDto>

    // RESERVATIONS global endpoints
    @GET("reservations")
    suspend fun listAllReservations(): List<ReservationDto>

    @GET("reservations/{res_id}")
    suspend fun getReservationById(
        @Path("res_id") reservationId: Int
    ): ReservationDto

    @DELETE("reservations/{res_id}")
    suspend fun cancelReservationById(
        @Path("res_id") reservationId: Int
    ): ResponseBodyDto
}
