package com.TravelPlanner.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "travel_table",
    foreignKeys = [
        ForeignKey(
            entity = User_Entities::class,
            parentColumns = ["username"],
            childColumns = ["user_owner"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Travel_Entities(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "travel_name") val title: String,
    @ColumnInfo(name = "destination") val location: String,
    @ColumnInfo(name= "description") val description:String,
    @ColumnInfo(name= "description") val valoracion:Float,
    @ColumnInfo(name = "start_date") val fechainicio: Long,
    @ColumnInfo(name = "end_date") val fechafinal: Long,
    @ColumnInfo(name = "user_owner") val userOwner: String
)


