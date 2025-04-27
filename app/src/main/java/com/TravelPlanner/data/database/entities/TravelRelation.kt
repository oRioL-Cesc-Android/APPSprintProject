package com.TravelPlanner.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TravelRelation(
    @Embedded val travel: Travel_Entities,

    @Relation(
        parentColumn = "userOwner",
        entityColumn = "username"
    )
    val user: User_Entities,

    @Relation(
        entity = Activites_Entities::class,
        parentColumn = "id",
        entityColumn = "travel_id"
    )
    val activities: List<Activites_Entities>
)
