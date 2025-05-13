package com.TravelPlanner.data.remote.dto

data class ReservationAvailabilityDto(
    val hotelId: Int,
    val roomType: String,
    val date: String, // Formato esperado: "YYYY-MM-DD"
    val isAvailable: Boolean,
    val availableRooms: Int
)
