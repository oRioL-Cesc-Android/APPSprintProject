package com.TravelPlanner.models

data class Reservation(
    val id: String,
    val hotel_id: String,
    val room_id: String,
    val start_date: String,
    val end_date: String,
    val guest_name: String,
    val guest_email: String,
    val hotel: Hotel,
    val room: Room
)