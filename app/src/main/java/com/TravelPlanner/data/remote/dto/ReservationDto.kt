package com.TravelPlanner.data.remote.dto

data class ReservationDto(
    val id: Int? = null,
    val hotelId: Int,
    val userId: Int,
    val roomType: String,
    val checkInDate: String,   // formato: "YYYY-MM-DD"
    val checkOutDate: String,  // formato: "YYYY-MM-DD"
    val guests: Int
)
