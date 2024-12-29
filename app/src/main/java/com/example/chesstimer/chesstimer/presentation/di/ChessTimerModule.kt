package com.example.chesstimer.chesstimer.presentation.di

import com.example.chesstimer.chesstimer.presentation.configure.SelectGameDurationViewModel
import com.example.chesstimer.chesstimer.presentation.timer.ChessTimerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chessTimerModule = module {
    viewModelOf(::ChessTimerViewModel)
    viewModelOf(::SelectGameDurationViewModel)
}