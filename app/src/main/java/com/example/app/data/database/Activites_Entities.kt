package com.example.app.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Travel_Entities::class,
            parentColumns = ["id"],
            childColumns = ["travel_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)


data class Activites_Entities (
    @PrimaryKey(autoGenerate = true)
    val activity_id: Int = 0,  // Int primary key
    val travel_id: Int,
    val nameActivity: String,
    val ubicacion: String,
    val duration: Int  // Should be Int
)
