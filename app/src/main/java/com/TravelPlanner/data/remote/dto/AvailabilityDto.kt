package com.TravelPlanner.data.remote.dto

data class AvailabilityDto(
    val hotelId: Int,
    val roomType: String,
    val available: Boolean,
    val availableRooms: Int
)
