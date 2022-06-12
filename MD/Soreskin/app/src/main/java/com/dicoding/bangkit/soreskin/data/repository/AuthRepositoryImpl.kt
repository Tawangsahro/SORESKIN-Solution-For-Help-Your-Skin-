package com.dicoding.bangkit.soreskin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dicoding.bangkit.soreskin.data.source.local.model.UserEntity
import com.dicoding.bangkit.soreskin.data.source.local.room.dao.SoreskinDao
import com.dicoding.bangkit.soreskin.util.Result
import com.dicoding.bangkit.soreskin.util.UserPreferences
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dao: SoreskinDao,
    private val prefs: UserPreferences
) : AuthRepository {

    override fun signUp(
        username: String,
        email: String,
        password: String
    ): LiveData<Result<UserEntity>> = liveData {
        emit(Result.Loading)
        try {
            val user = UserEntity(username = username, email = email, password = password)
            dao.signUp(user)

            val data = dao.signIn(email, password)

            prefs.login(data.email)

            emit(Result.Success(data))
        } catch (e: IOException) {
            emit(Result.Error("Failed to create user"))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage?.toString() ?: "Unknown error"))
        }
    }

    override fun signIn(
        email: String,
        password: String
    ): LiveData<Result<UserEntity>> = liveData {
        emit(Result.Loading)
        try {
            val data = dao.signIn(email, password)
            prefs.login(data.email)
            emit(Result.Success(data))
        } catch (e: IOException) {
            emit(Result.Error("Failed to sign in"))
        } catch (e: NullPointerException) {
            emit(Result.Error("User not found"))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage?.toString() ?: "Unknown error"))
        }
    }

    override fun getUser(email: String): LiveData<Result<UserEntity>> =
        liveData {
            emit(Result.Loading)
            try {
                val data = dao.getUser(email)
                emit(Result.Success(data))
            } catch (e: IOException) {
                emit(Result.Error("Failed to retrieve data"))
            } catch (e: NullPointerException) {
                emit(Result.Error("User not found"))
            } catch (e: Exception) {
                emit(Result.Error(e.localizedMessage?.toString() ?: "Unknown error"))
            }
        }

    override fun getAuthenticatedUser() = prefs.getAuthenticatedEmail().asLiveData()

    override fun logout() = prefs.logout()
}