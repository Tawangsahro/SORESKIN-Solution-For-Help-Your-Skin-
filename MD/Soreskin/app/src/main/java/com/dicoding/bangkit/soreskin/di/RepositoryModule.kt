package com.dicoding.bangkit.soreskin.di

import com.dicoding.bangkit.soreskin.data.repository.AuthRepository
import com.dicoding.bangkit.soreskin.data.repository.AuthRepositoryImpl
import com.dicoding.bangkit.soreskin.data.source.local.room.SoreskinDatabase
import com.dicoding.bangkit.soreskin.util.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(database: SoreskinDatabase, prefs: UserPreferences): AuthRepository =
        AuthRepositoryImpl(database.soreskinDao(), prefs)
}