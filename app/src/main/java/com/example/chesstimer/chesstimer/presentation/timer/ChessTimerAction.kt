package com.example.chesstimer.chesstimer.presentation.timer

sealed interface ChessTimerAction {

    data object OnPlayerOneTimerClicked : ChessTimerAction

    data object OnPlayerTwoTimerClicked : ChessTimerAction

    data object OnPlayPausedButtonClicked : ChessTimerAction

    data object OnRestartGamePlayClicked : ChessTimerAction

    data object OnSelectGameDurationTimerClicked : ChessTimerAction
}