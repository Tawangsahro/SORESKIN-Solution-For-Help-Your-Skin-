package com.dicoding.bangkit.soreskin.di

import android.app.Application
import androidx.room.Room
import com.dicoding.bangkit.soreskin.data.source.local.room.SoreskinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSoreskinDatabase(app: Application): SoreskinDatabase =
        Room.databaseBuilder(app, SoreskinDatabase::class.java, "soreskin_db").build()
}