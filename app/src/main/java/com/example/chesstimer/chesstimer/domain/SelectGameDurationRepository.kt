package com.example.chesstimer.chesstimer.domain

import kotlinx.coroutines.flow.Flow

interface SelectGameDurationRepository {

    val gameDurations: Flow<List<GameDuration>>

    suspend fun updateGameDuration(duration: Long)
}