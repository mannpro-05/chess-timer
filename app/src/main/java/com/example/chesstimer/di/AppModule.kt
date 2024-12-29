package com.example.chesstimer.di

import com.example.chesstimer.ChessTimerApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as ChessTimerApp).applicationScope
    }
}
