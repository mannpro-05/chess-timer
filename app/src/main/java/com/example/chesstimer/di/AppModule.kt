package com.example.chesstimer.di

import com.example.chesstimer.ChessTimerApp
import com.example.chesstimer.chesstimer.presentation.timer.ChessTimerViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as ChessTimerApp).applicationScope
    }
}
