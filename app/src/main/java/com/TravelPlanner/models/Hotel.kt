package com.TravelPlanner.models

import androidx.room.Room

data class Hotel(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: List<Room>,
    val image_url: String
)