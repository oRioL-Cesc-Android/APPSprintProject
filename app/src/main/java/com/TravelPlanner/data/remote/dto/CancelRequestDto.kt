package com.TravelPlanner.data.remote.dto

data class CancelRequestDto(
    val reservationId: Int,
    val reason: String? = null
)
