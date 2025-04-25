package com.TravelPlanner.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User_Entities(
    @PrimaryKey
    val username: String,
    val email: String,
    val password: String
)
