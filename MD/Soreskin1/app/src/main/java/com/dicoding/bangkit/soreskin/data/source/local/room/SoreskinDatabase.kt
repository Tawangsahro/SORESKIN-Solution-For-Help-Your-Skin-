package com.dicoding.bangkit.soreskin.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.bangkit.soreskin.data.source.local.model.HistoryEntity
import com.dicoding.bangkit.soreskin.data.source.local.room.dao.SoreskinDao

@Database(
    entities = [HistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SoreskinDatabase : RoomDatabase() {

    abstract fun soreskinDao(): SoreskinDao
}