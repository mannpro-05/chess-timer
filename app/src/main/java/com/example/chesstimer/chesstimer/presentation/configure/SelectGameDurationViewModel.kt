package com.example.chesstimer.chesstimer.presentation.configure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chesstimer.chesstimer.domain.GameDuration
import com.example.chesstimer.chesstimer.domain.SelectGameDurationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private var selectedPosition: Int = -1

    private val _event = Channel<SelectGameDurationEvent>()
    val event = _event.receiveAsFlow()

    private var _state = MutableStateFlow(
        SelectGameDurationState()
    )

    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SelectGameDurationState()
        )

    init {
        repository.gameDurations.onEach { storedGameDurations ->
            _state.update {
                it.copy(
                    durations = storedGameDurations.toUiDurationList(),
                    editableItems = storedGameDurations.toUiDurationList().toEditableItems(),
                    selectedDurationPosition = findSelectedPos()
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

            is SelectGameDurationAction.OnEditItemChecked -> {
                _state.update {
                    it.copy(
                        editableItems = it.editableItems.mapIndexed { index, item ->
                            if (index == action.itemPosition) {
                                item.copy(
                                    isSelected = action.isChecked
                                )
                            } else {
                                item
                            }
                        }
                    )
                }
                Log.d("zzz", "zzz ${_state.value.editableItems}")
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

            SelectGameDurationAction.OnEditButtonClicked -> {
                _state.update {
                    it.copy(
                        isInEditMode = true
                    )
                }
            }

            SelectGameDurationAction.OnFinishEditButtonClicked -> {
                _state.update {
                    it.copy(
                        isInEditMode = false
                    )
                }
            }
        }
    }

    private fun updateGameDuration() {
        viewModelScope.launch {
            val duration = repository.gameDurations.first()[selectedPosition]
            repository.updateGameDuration(duration.gameDuration)
            _event.send(SelectGameDurationEvent.GameDurationUpdated)
        }
    }

    private fun findSelectedPos(): Int = 0
}

private fun List<GameDuration>.toUiDurationList(): List<String> = map {
    it.gameDurationName
}

private fun List<String>.toEditableItems(): List<EditableItem> = map {
    EditableItem(
        item = it,
        isSelected = false
    )
}
