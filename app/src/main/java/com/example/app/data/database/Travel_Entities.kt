package com.example.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Travel_Entities(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var location: String,
    var description: String,
    var rating: Float,
    var fechainicio: Long,
    var fechafinal: Long
    //var activities: List<Activitys> = emptyList(),

    )

