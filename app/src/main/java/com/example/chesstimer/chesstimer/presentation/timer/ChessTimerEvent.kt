package com.example.chesstimer.chesstimer.presentation.timer

sealed interface ChessTimerEvent {
    data class LaunchSelectGameDuration(
        val isGameRunning: Boolean,
        val currentGameTime: Long
    ) : ChessTimerEvent
}