package com.TravelPlanner.data.remote.dto

data class ReservationResponseBody (
    val message: String,
    val nights: Int,
    val reservation: ReservationDto
)