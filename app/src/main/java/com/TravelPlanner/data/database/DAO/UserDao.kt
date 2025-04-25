package com.TravelPlanner.data.database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.TravelPlanner.data.database.entities.User_Entities

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User_Entities): Long

    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User_Entities?


}
