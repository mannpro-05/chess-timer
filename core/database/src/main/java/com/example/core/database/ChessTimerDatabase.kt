package com.example.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.database.dao.SupportedGameDurationsDao
import com.example.core.database.entity.SupportedGameDurationEntity

@Database(
    entities = [SupportedGameDurationEntity::class],
    version = 1
)
abstract class ChessTimerDatabase : RoomDatabase() {
    abstract val gameDurationDao: SupportedGameDurationsDao
}