package com.TravelPlanner.data.remote.api

import retrofit2.Response
import retrofit2.http.*
import com.TravelPlanner.data.remote.dto.*
import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.ReserveRequest

interface HotelApiService {

    @GET("/hotels/{group_id}/hotels")
    suspend fun getHotels(
        @Path("group_id") groupId: String
    ): List<Hotel>

    @GET("/hotels/{group_id}/availability")
    suspend fun checkAvailability(
        @Path("group_id") groupId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("city") city: String? = null
    ): List<Hotel>

    @POST("/hotels/{group_id}/reserve")
    suspend fun reserveRoom(
        @Path("group_id") groupId: String,
        @Body request: ReserveRequest
    ): Response<Unit>
}