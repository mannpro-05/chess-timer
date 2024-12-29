package com.example.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SupportedGameDurationEntity(
    @PrimaryKey(autoGenerate = true)
    val gameDurationId: Int = 0,
    val durationName: String,
    val gameDuration: Long,
    val incrementDuration: Long,
    val order: Int
)
