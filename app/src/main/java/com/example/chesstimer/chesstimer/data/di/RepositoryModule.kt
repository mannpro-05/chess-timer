package com.example.chesstimer.chesstimer.data.di

import com.example.chesstimer.chesstimer.data.SelectGameDurationRepositoryImpl
import com.example.chesstimer.chesstimer.data.UserPreferenceGameDuration
import com.example.chesstimer.chesstimer.domain.ChessGameDurationRepository
import com.example.chesstimer.chesstimer.domain.SelectGameDurationRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::UserPreferenceGameDuration).bind<ChessGameDurationRepository>()
    singleOf(::SelectGameDurationRepositoryImpl).bind<SelectGameDurationRepository>()
}
