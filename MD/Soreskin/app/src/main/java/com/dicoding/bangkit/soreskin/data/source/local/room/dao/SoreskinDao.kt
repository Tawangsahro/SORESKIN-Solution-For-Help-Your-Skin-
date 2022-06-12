package com.dicoding.bangkit.soreskin.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.bangkit.soreskin.data.source.local.model.UserEntity

@Dao
interface SoreskinDao {

    @Insert
    suspend fun signUp(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun signIn(email: String, password: String): UserEntity

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUser(email: String): UserEntity
}