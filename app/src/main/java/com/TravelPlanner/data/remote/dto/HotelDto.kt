package com.TravelPlanner.data.remote.dto

data class HotelDto(
    val id: Int,
    val name: String,
    val location: String,
    val rating: Float,
    val availableRooms: Int
)
