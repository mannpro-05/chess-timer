package com.example.chesstimer.chesstimer.domain

import kotlinx.coroutines.flow.Flow

interface ChessGameDurationRepository {
    val gameDuration: Flow<Long>

    suspend fun updateGameDuration(duration: Long)
}