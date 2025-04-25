package com.TravelPlanner.data.repository

import com.TravelPlanner.data.database.DAO.UserDao
import com.TravelPlanner.data.database.entities.User_Entities
import javax.inject.Inject



class User_Repo @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun registerUser(user: User_Entities): Result<Boolean> {
        return try {
            val existing = userDao.getUserByUsername(user.username)
            if (existing != null) {
                return Result.failure(Exception("El nombre de usuario ya existe"))
            }

            userDao.insertUser(user)
            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun getUserByUsername(username: String): User_Entities? {
        return userDao.getUserByUsername(username)
    }
}
