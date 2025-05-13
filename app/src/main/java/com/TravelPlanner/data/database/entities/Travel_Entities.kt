package com.TravelPlanner.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.TravelPlanner.data.database.util.StringListConverter

@Entity
@TypeConverters(StringListConverter::class)
data class Travel_Entities(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var location: String,
    var description: String,
    var rating: Float,
    var fechainicio: Long,
    var fechafinal: Long,
    var userName: String, // Nueva columna que almacena el username del usuario que creó el viaje
    var imagePaths: List<String> = emptyList() // Lista de rutas de imágenes
)

