package com.example.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.app.ui.screens.Activitys

@Entity
data class Travel_Entities(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var location: String,
    var description: String,
    var rating: Float,
    var duration: String,
    //var activities: List<Activitys> = emptyList(),

    )

