package com.example.chesstimer.chesstimer.presentation.timer

data class ChessTimerState(
    val playerOneRemainingTime: String = "",
    val playerTwoRemainingTime: String = "",
    val isPlayerOneTimerActive: Boolean = false,
    val isPlayerTwoTimerActive: Boolean = false,
    val isPlayerOneTimeOutReached: Boolean = false,
    val isPlayerTwoTimeOutReached: Boolean = false,
    val isTimerRunning: Boolean = false
)
