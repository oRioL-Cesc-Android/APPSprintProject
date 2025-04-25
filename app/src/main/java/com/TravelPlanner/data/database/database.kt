package com.TravelPlanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.TravelPlanner.data.database.DAO.DAO_Activity
import com.TravelPlanner.data.database.DAO.DAO_Travel
import com.TravelPlanner.data.database.DAO.UserDao
import com.TravelPlanner.data.database.entities.Activites_Entities
import com.TravelPlanner.data.database.entities.Travel_Entities
import com.TravelPlanner.data.database.entities.User_Entities

@Database(
    entities = [
        Travel_Entities::class,
        Activites_Entities::class,
        User_Entities::class
      ],
    version = 1
)
abstract class database: RoomDatabase(){
    abstract fun ObtenerDao(): DAO_Travel
    abstract fun ObtenerActivityDao(): DAO_Activity
    abstract fun userDao(): UserDao
}