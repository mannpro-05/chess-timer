package com.example.core.database.di

import androidx.room.Room
import com.example.core.database.ChessTimerDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ChessTimerDatabase::class.java,
            "chessTimer.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<ChessTimerDatabase>().gameDurationDao }
}