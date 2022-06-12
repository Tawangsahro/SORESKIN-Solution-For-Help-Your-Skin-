package com.dicoding.bangkit.soreskin.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.bangkit.soreskin.data.source.local.model.UserEntity
import com.dicoding.bangkit.soreskin.util.Result

interface AuthRepository {

    fun signUp(username: String, email: String, password: String): LiveData<Result<UserEntity>>

    fun signIn(email: String, password: String): LiveData<Result<UserEntity>>

    fun getUser(email: String): LiveData<Result<UserEntity>>

    fun getAuthenticatedUser(): LiveData<String>

    fun logout()
}