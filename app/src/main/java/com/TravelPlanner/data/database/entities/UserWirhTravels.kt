package com.TravelPlanner.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithTravels(
    @Embedded val user: User_Entities,  // El usuario logeado
    @Relation(
        entity = Travel_Entities::class,  // Entidad de los viajes
        parentColumn = "username",        // El campo que hace referencia al usuario (en User_Entities)
        entityColumn = "userName"         // El campo que está en Travel_Entities que apunta al usuario
    )
    val travels: List<Travel_Entities>  // Lista de los viajes del usuario
)
