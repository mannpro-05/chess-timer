package com.example.chesstimer.chesstimer.presentation.configure

sealed interface SelectGameDurationAction {

    data class OnDurationSelected(val selectedDurationPosition: Int) : SelectGameDurationAction

    data class OnEditItemChecked(val itemPosition: Int, val isChecked: Boolean) :
        SelectGameDurationAction

    data object OnStartButtonClicked : SelectGameDurationAction

    data object OnNewGameDurationRejected : SelectGameDurationAction

    data object OnConfirmNewGameDuration : SelectGameDurationAction

    data object OnEditButtonClicked : SelectGameDurationAction

    data object OnFinishEditButtonClicked : SelectGameDurationAction
}
