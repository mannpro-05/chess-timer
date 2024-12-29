package com.example.chesstimer.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore(name = "chess_game_time")

val dataStoreModule = module {
    single { get<Context>().dataStore }
}
