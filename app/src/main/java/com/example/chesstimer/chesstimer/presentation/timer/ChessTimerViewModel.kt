package com.example.chesstimer.chesstimer.presentation.timer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chesstimer.chesstimer.domain.ChessGameDurationRepository
import com.example.chesstimer.chesstimer.presentation.timer.mapper.toDisplayableTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class ChessTimerViewModel(private val repository: ChessGameDurationRepository) : ViewModel() {

    private var _state = MutableStateFlow(
        ChessTimerState()
    )

    private var isTimerRunning: Boolean = false

    private val isTimeoutReached = MutableStateFlow(false)

    private var countDownJob: Job? = null

    private val playerOneRemainingTime = MutableStateFlow(
        0L
    )

    private val playerTwoRemainingTime = MutableStateFlow(
        0L
    )

    private var currentGameTime = 0L

    private val _events = Channel<ChessTimerEvent>()
    val events = _events.receiveAsFlow()

    val state = _state.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), ChessTimerState()
    )

    init {
        repository.gameDuration.onEach { userPreferredGameTime ->
            currentGameTime = userPreferredGameTime
            resetTimer()
        }.launchIn(viewModelScope)

        playerOneRemainingTime.onEach { remainingTime ->
            _state.update {
                it.copy(
                    playerOneRemainingTime = remainingTime.toDisplayableTime()
                )
            }
        }.launchIn(viewModelScope)

        playerTwoRemainingTime.onEach { remainingTime ->
            _state.update {
                it.copy(
                    playerTwoRemainingTime = remainingTime.toDisplayableTime()
                )
            }
        }.launchIn(viewModelScope)

        isTimeoutReached
            .filter { it }
            .onEach {
                _state.update {
                    it.copy(
                        isPlayerOneTimeOutReached = playerOneRemainingTime.value == 0L,
                        isPlayerTwoTimeOutReached = playerTwoRemainingTime.value == 0L,
                        isTimerRunning = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun resetTimer() {
        countDownJob?.cancel()
        isTimerRunning = false
        playerOneRemainingTime.update { currentGameTime.milliseconds.inWholeMilliseconds }
        playerTwoRemainingTime.update { currentGameTime.milliseconds.inWholeMilliseconds }
        _state.update {
            it.copy(
                isPlayerOneTimerActive = false,
                isPlayerTwoTimerActive = false,
                isPlayerOneTimeOutReached = false,
                isPlayerTwoTimeOutReached = false,
                isTimerRunning = false
            )
        }
    }

    private fun startPlayerOneTimer() {
        startPlayerTimer(
            playerOneRemainingTime
        )
        _state.update {
            it.copy(
                isPlayerOneTimerActive = true,
                isPlayerTwoTimerActive = false
            )
        }
    }

    private fun startPlayerTwoTimer() {
        startPlayerTimer(
            playerTwoRemainingTime
        )
        _state.update {
            it.copy(
                isPlayerOneTimerActive = false,
                isPlayerTwoTimerActive = true
            )
        }
    }

    fun onAction(action: ChessTimerAction) {
        if (isTimeoutReached.value && (action != ChessTimerAction.OnRestartGamePlayClicked || action != ChessTimerAction.OnSelectGameDurationTimerClicked)) return
        when (action) {
            ChessTimerAction.OnPlayerOneTimerClicked -> {
                startPlayerTwoTimer()
            }

            ChessTimerAction.OnPlayerTwoTimerClicked -> {
                startPlayerOneTimer()
            }

            ChessTimerAction.OnPlayPausedButtonClicked -> {
                if (isTimerRunning) {
                    countDownJob?.cancel()
                    isTimerRunning = false
                } else {
                    if (_state.value.isPlayerTwoTimerActive) {
                        startPlayerTwoTimer()
                    } else {
                        startPlayerOneTimer()
                    }
                    isTimerRunning = true
                }
            }

            ChessTimerAction.OnRestartGamePlayClicked -> {
                resetTimer()
            }

            ChessTimerAction.OnSelectGameDurationTimerClicked -> {
                countDownJob?.cancel()
                isTimerRunning = false
                viewModelScope.launch {
                    _events.send(
                        ChessTimerEvent.LaunchSelectGameDuration(
                            isGameRunning = isGameRunning(),
                            currentGameTime = currentGameTime
                        )
                    )
                }
            }
        }
        _state.update {
            it.copy(
                isTimerRunning = isTimerRunning
            )
        }
    }

    private fun isGameRunning(): Boolean =
        isTimerRunning || (playerOneRemainingTime.value != currentGameTime || playerTwoRemainingTime.value != currentGameTime)

    private fun startPlayerTimer(
        flow: MutableStateFlow<Long>,
    ) {
        countDownJob?.cancel()
        isTimerRunning = true
        countDownJob = viewModelScope.launch {
            while (flow.value > 0) {
                delay(10L)
                flow.update { it - 10L }
            }
            isTimeoutReached.update { true }
            isTimerRunning = false
        }
    }
}