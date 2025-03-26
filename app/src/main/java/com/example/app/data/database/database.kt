package com.example.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Travel_Entities::class,
        Activites_Entities::class
      ],
    version = 1
)
abstract class database: RoomDatabase(){
    abstract fun ObtenerDao(): DAO_Travel
    abstract fun ObtenerActivityDao(): DAO_Activity
}