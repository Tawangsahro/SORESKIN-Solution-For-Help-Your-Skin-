package com.dicoding.bangkit.soreskin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.bangkit.soreskin.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val auth = repository.getAuthenticatedUser()

    fun signUp(username: String, email: String, password: String) =
        repository.signUp(username, email, password)

    fun signIn(email: String, password: String) =
        repository.signIn(email, password)

    fun getUser(email: String) =
        repository.getUser(email)

    fun logout() = repository.logout()
}