package com.TravelPlanner.models

data class Room(
    val id: String,
    val room_type: String,
    val price: Double,
    val image_url: List<String>
)