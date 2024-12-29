package com.example.chesstimer.chesstimer.presentation.configure

data class SelectGameDurationState(
    val durations: List<String> = emptyList(),
    val editableItems: List<EditableItem> = emptyList(),
    val selectedDurationPosition: Int = 0,
    val shouldShowConfirmationDialog: Boolean = false,
    val isInEditMode: Boolean = false
)
