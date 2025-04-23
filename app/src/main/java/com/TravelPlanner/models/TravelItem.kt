package com.TravelPlanner.models


data class TravelItem(
    val id: Int,
    var title: String,
    var location: String,
    var description: String,
    var rating: Float,
    var fechainicio: Long,
    var fechafinal: Long,
    var activities: List<ActivityItems> = emptyList(),


    )