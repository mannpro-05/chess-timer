package com.example.chesstimer.chesstimer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.chesstimer.chesstimer.data.mapper.toGameDuration
import com.example.chesstimer.chesstimer.domain.GameDuration
import com.example.chesstimer.chesstimer.domain.SelectGameDurationRepository
import com.example.core.database.dao.SupportedGameDurationsDao
import com.example.core.database.entity.SupportedGameDurationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SelectGameDurationRepositoryImpl(
    applicationScope: CoroutineScope,
    private val gameDurationDao: SupportedGameDurationsDao,
    private val dataStore: DataStore<Preferences>
) : SelectGameDurationRepository {

    private val _gameDuration = MutableStateFlow<List<GameDuration>>(emptyList())
    override val gameDurations: Flow<List<GameDuration>> = _gameDuration.asSharedFlow()

    init {
        gameDurationDao
            .getGameDurations()
            .onEach { storedGameDurations ->
                if (storedGameDurations.isEmpty()) {
                    insertRules()
                }
                _gameDuration.update { storedGameDurations.toGameDuration() }
            }
            .launchIn(applicationScope)
    }

    private val defaultGameTimes = listOf(
        Pair(1.minutes.inWholeMilliseconds, 0.seconds.inWholeMilliseconds),
        Pair(1.minutes.inWholeMilliseconds, 1.seconds.inWholeMilliseconds),
        Pair(2.minutes.inWholeMilliseconds, 0.seconds.inWholeMilliseconds),
        Pair(2.minutes.inWholeMilliseconds, 1.seconds.inWholeMilliseconds),
        Pair(3.minutes.inWholeMilliseconds, 0.seconds.inWholeMilliseconds),
        Pair(3.minutes.inWholeMilliseconds, 2.seconds.inWholeMilliseconds),
        Pair(5.minutes.inWholeMilliseconds, 0.seconds.inWholeMilliseconds),
        Pair(5.minutes.inWholeMilliseconds, 5.seconds.inWholeMilliseconds),
        Pair(10.minutes.inWholeMilliseconds, 0.seconds.inWholeMilliseconds),
        Pair(15.minutes.inWholeMilliseconds, 10.seconds.inWholeMilliseconds),
        Pair(20.minutes.inWholeMilliseconds, 0.seconds.inWholeMilliseconds),
        Pair(30.minutes.inWholeMilliseconds, 0.seconds.inWholeMilliseconds)
    )

    override suspend fun updateGameDuration(duration: Long) {
        dataStore.edit { preferences ->
            preferences[GAME_DURATION] = duration
        }
    }

    private suspend fun insertRules() {
        val data = buildList<SupportedGameDurationEntity> {
            defaultGameTimes.forEachIndexed { index, time ->
                val durationName = StringBuilder()
                durationName.append("${time.first.milliseconds.inWholeMinutes} min")
                if (time.second != 0L) {
                    durationName.append(" | ${time.second.milliseconds.inWholeSeconds} sec")
                }
                add(
                    SupportedGameDurationEntity(
                        durationName = durationName.toString(),
                        gameDuration = time.first,
                        incrementDuration = time.second,
                        order = index + 1
                    )
                )
            }
        }

        gameDurationDao.upsertGameDurations(data)
    }

    companion object {
        val GAME_DURATION = longPreferencesKey("game_duration")
    }
}