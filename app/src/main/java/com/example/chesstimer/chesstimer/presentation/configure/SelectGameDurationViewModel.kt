package com.example.chesstimer.chesstimer.presentation.configure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chesstimer.chesstimer.domain.ChessGameDurationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class SelectGameDurationViewModel(
    private val isGameRunning: Boolean,
    private val repository: ChessGameDurationRepository
) : ViewModel() {

    private var _state = MutableStateFlow(
        SelectGameDurationState()
    )

    private var selectedPosition: Int = 0

    private val _event = Channel<SelectGameDurationEvent>()
    val event = _event.receiveAsFlow()

    private val supportedDurationList: List<Long> = listOf(
        1.minutes.inWholeMilliseconds,
        2.minutes.inWholeMilliseconds,
        3.minutes.inWholeMilliseconds,
        5.minutes.inWholeMilliseconds,
        10.minutes.inWholeMilliseconds,
        15.minutes.inWholeMilliseconds,
        20.minutes.inWholeMilliseconds,
        30.minutes.inWholeMilliseconds,
        45.minutes.inWholeMilliseconds,
        60.minutes.inWholeMilliseconds,
    )

    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SelectGameDurationState()
        )

    init {
        repository.gameDuration
            .onEach { currentGameDuration ->
                selectedPosition = supportedDurationList.indexOfFirst { supportedDuration ->
                    supportedDuration == currentGameDuration
                }.takeIf { index -> index != -1 } ?: 0
                _state.update {
                    it.copy(
                        durations = supportedDurationList.toUiDurationList(),
                        selectedDurationPosition = selectedPosition
                    )
                }
            }.launchIn(viewModelScope)
    }

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
        val duration = supportedDurationList[selectedPosition]
        viewModelScope.launch {
            repository.updateGameDuration(duration)
            _event.send(SelectGameDurationEvent.GameDurationUpdated)
        }
    }
}

private fun List<Long>.toUiDurationList(): List<String> = map {
    "${it.milliseconds.inWholeMinutes} min"
}