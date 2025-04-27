package com.TravelPlanner.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "access_log")
data class AccessLog_Entities(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val accessTime: Long,
    val isLogin: Boolean // true = login, false = logout
)
