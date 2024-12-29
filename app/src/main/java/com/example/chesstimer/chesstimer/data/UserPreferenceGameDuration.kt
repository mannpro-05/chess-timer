package com.example.chesstimer.chesstimer.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.chesstimer.chesstimer.domain.ChessGameDurationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class UserPreferenceGameDuration(
    applicationScope: CoroutineScope,
    private val dataStore: DataStore<Preferences>
) : ChessGameDurationRepository {
    private val _timer: MutableSharedFlow<Long> = MutableStateFlow(0L)
    override val gameDuration: Flow<Long> = _timer.asSharedFlow()

    init {
        dataStore.data.map { preferences ->
            preferences[GAME_DURATION]?.toLong() ?: 0L
        }.onEach {
            _timer.emit(it)
        }.launchIn(applicationScope)

        applicationScope.launch {
            dataStore.setDefaultValue(
                GAME_DURATION,
                10.minutes.inWholeMilliseconds
            )
        }
    }

    override suspend fun updateGameDuration(duration: Long) {
        dataStore.edit { preferences ->
            preferences[GAME_DURATION] = duration
        }
    }

    private suspend fun DataStore<Preferences>.setDefaultValue(
        key: Preferences.Key<Long>,
        defaultValue: Long
    ) {
        val value = this.data.map { preferences -> preferences[key] }.first()
        if (value == null) {
            this.edit { preferences ->
                preferences[key] = defaultValue
            }
        }
    }

    companion object {
        val GAME_DURATION = longPreferencesKey("game_duration")
    }
}