package com.TravelPlanner.data.database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.TravelPlanner.data.database.entities.AccessLog_Entities

@Dao
interface AccessLogDao {

    @Insert
    suspend fun insertAccessLog(log: AccessLog_Entities)

    @Query("SELECT * FROM access_log WHERE username = :username ORDER BY accessTime DESC")
    suspend fun getAccessLogsForUser(username: String): List<AccessLog_Entities>

    @Query("DELETE FROM access_log")
    suspend fun clearAllLogs()
}
