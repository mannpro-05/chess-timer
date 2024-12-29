package com.example.chesstimer.chesstimer.presentation.configure

data class SelectGameDurationState(
    val durations: List<String> = emptyList(),
    val selectedDurationPosition: Int = 0,
    val shouldShowConfirmationDialog: Boolean = false
)
