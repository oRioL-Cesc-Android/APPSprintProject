package com.TravelPlanner.models

data class ActivityItems (
    val activity_id: Int = 0,  // Added this field
    val nameActivity: String,
    val ubicacion: String,
    val duration: Int  // Changed from String to Int to match your database
)