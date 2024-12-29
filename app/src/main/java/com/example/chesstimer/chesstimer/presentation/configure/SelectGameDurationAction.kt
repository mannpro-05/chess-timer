package com.example.chesstimer.chesstimer.presentation.configure

sealed interface SelectGameDurationAction {

    data class OnDurationSelected(val selectedDurationPosition: Int) : SelectGameDurationAction

    data object OnStartButtonClicked : SelectGameDurationAction

    data object OnNewGameDurationRejected : SelectGameDurationAction

    data object OnConfirmNewGameDuration : SelectGameDurationAction
}
