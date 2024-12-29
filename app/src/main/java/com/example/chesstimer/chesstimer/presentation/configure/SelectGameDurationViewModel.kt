package com.example.chesstimer.chesstimer.presentation.configure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chesstimer.chesstimer.domain.SelectGameDurationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SelectGameDurationViewModel(
    private val isGameRunning: Boolean,
    private val currentGameDuration: Long,
    private val repository: SelectGameDurationRepository
) : ViewModel() {

    private var selectedPosition: Int = 0

    private val _event = Channel<SelectGameDurationEvent>()
    val event = _event.receiveAsFlow()

    private var _state = MutableStateFlow(
        SelectGameDurationState(
            durations = repository.provideSupportedGameDurations().toUiDurationList(),
            selectedDurationPosition = findSelectedPos()
        )
    )

    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SelectGameDurationState(
                durations = repository.provideSupportedGameDurations().toUiDurationList(),
                selectedDurationPosition = findSelectedPos()
            )
        )

    fun onAction(action: SelectGameDurationAction) {
        when (action) {
            SelectGameDurationAction.OnStartButtonClicked -> {
                if (isGameRunning) {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                shouldShowConfirmationDialog = true
                            )
                        }
                    }
                    return
                }
                updateGameDuration()
            }

            SelectGameDurationAction.OnConfirmNewGameDuration -> {
                _state.update {
                    it.copy(
                        shouldShowConfirmationDialog = false
                    )
                }
                updateGameDuration()
            }

            is SelectGameDurationAction.OnDurationSelected -> {
                selectedPosition = action.selectedDurationPosition
            }

            SelectGameDurationAction.OnNewGameDurationRejected -> {
                _state.update {
                    it.copy(
                        shouldShowConfirmationDialog = false
                    )
                }
            }
        }
    }

    private fun updateGameDuration() {
        val duration = repository.provideSupportedGameDurations()[selectedPosition]
        viewModelScope.launch {
            repository.updateGameDuration(duration)
            _event.send(SelectGameDurationEvent.GameDurationUpdated)
        }
    }

    private fun findSelectedPos(): Int =
        repository.provideSupportedGameDurations().indexOfFirst { supportedDuration ->
            supportedDuration == currentGameDuration
        }.takeIf { index -> index != -1 } ?: 0

}

private fun List<Long>.toUiDurationList(): List<String> = map {
    "${it.milliseconds.inWholeMinutes} min"
}