package com.TravelPlanner.data.remote.dto



import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.Room
import com.google.gson.annotations.SerializedName

data class ReservationDto(
    val id: String,
    val hotelId: String,
    val roomId: String,
    val startDate: String,
    val endDate: String,
    val guestName: String,
    val guestEmail: String,
    val hotel: Hotel,
    val room: Room
)