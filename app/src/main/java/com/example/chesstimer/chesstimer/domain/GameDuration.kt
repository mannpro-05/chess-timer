package com.example.chesstimer.chesstimer.domain

data class GameDuration(
    val gameDurationName: String,
    val gameDuration: Long,
    val incrementDuration: Long,
    val order: Int
)
