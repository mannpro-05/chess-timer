package com.example.chesstimer.chesstimer.presentation.configure

sealed interface SelectGameDurationEvent {
    data object GameDurationUpdated : SelectGameDurationEvent
}