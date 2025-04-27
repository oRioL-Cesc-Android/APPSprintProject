package com.TravelPlanner.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User_Entities(
    @PrimaryKey
    val username: String,
    val email: String,
    val password: String,
    val address: String,
    val birthDate: Long?,
    val country: String,
    val phoneNumber: String,
    val acceptReceiveEmails: Boolean 
)
