package com.example.chesstimer.chesstimer.domain

interface SelectGameDurationRepository {

    fun provideSupportedGameDurations(): List<Long>

    suspend fun updateGameDuration(duration: Long)
}