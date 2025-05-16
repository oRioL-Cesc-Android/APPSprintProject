package com.TravelPlanner.models

import androidx.room.Room

data class Hotel(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val imageUrl: String,
    val rooms: List<Room>? = emptyList()
)