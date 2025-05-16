package com.TravelPlanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.TravelPlanner.data.database.DAO.DAO_Activity
import com.TravelPlanner.data.database.DAO.DAO_Travel
import com.TravelPlanner.data.database.DAO.UserDao
import com.TravelPlanner.data.database.entities.Activites_Entities
import com.TravelPlanner.data.database.entities.Travel_Entities
import com.TravelPlanner.data.database.entities.User_Entities
import com.TravelPlanner.data.database.util.StringListConverter

@Database(
    entities = [
        Travel_Entities::class,
        Activites_Entities::class,
        User_Entities::class
      ],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class database: RoomDatabase(){
    abstract fun ObtenerDao(): DAO_Travel
    abstract fun ObtenerActivityDao(): DAO_Activity
    abstract fun userDao(): UserDao
}
