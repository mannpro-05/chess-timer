package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.core.database.entity.SupportedGameDurationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupportedGameDurationsDao {
    /**
     * Inserts a run or updates it if it's primary key exists
     */
    @Upsert
    suspend fun upsertGameDuration(run: SupportedGameDurationEntity)

    @Upsert
    suspend fun upsertGameDurations(runs: List<SupportedGameDurationEntity>)

    @Query("SELECT * FROM supportedgamedurationentity ORDER BY `order` ASC")
    fun getGameDurations(): Flow<SupportedGameDurationEntity>

    @Query("DELETE FROM supportedgamedurationentity WHERE gameDurationId = :id")
    suspend fun deleteGameDuration(id: Int)

    @Query("DELETE FROM supportedgamedurationentity WHERE gameDurationId = :ids")
    suspend fun deleteGameDurations(ids: List<Int>)
}
