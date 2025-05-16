package com.TravelPlanner.data.remote.dto


import com.google.gson.annotations.SerializedName

data class AvailabilityDto(
    @SerializedName("available_hotels")
    val availableHotels: List<HotelDto>
)