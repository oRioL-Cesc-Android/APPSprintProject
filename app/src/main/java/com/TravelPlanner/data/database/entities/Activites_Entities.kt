package com.TravelPlanner.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Activites_Entities",
    foreignKeys = [
        ForeignKey(
            entity = Travel_Entities::class,
            parentColumns = ["id"],
            childColumns = ["travel_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)



data class Activites_Entities(
    @PrimaryKey(autoGenerate = true) val activity_id: Int = 0, // Asegúrate de que esto esté correcto
    @ColumnInfo(name = "travel_id") val travel_id: Int,
    @ColumnInfo(name = "nameActivity") val nameActivity: String,
    @ColumnInfo(name = "ubicacion") val ubicacion: String,
    @ColumnInfo(name = "duration") val duration: Int
)


