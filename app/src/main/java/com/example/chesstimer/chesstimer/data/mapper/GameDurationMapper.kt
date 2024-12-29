package com.example.chesstimer.chesstimer.data.mapper

import com.example.chesstimer.chesstimer.domain.GameDuration
import com.example.core.database.entity.SupportedGameDurationEntity

fun List<SupportedGameDurationEntity>.toGameDuration(): List<GameDuration> = map {
    GameDuration(
        gameDurationName = it.durationName,
        gameDuration = it.gameDuration,
        incrementDuration = it.incrementDuration,
        order = it.order
    )
}