package com.example.chesstimer.chesstimer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.chesstimer.chesstimer.domain.SelectGameDurationRepository
import kotlin.time.Duration.Companion.minutes

class SelectGameDurationRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SelectGameDurationRepository {
    override fun provideSupportedGameDurations(): List<Long> = listOf(
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

    override suspend fun updateGameDuration(duration: Long) {
        dataStore.edit { preferences ->
            preferences[GAME_DURATION] = duration
        }
    }

    companion object {
        val GAME_DURATION = longPreferencesKey("game_duration")
    }
}