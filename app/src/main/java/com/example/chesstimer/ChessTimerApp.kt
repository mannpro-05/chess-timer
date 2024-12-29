package com.example.chesstimer

import android.app.Application
import com.example.chesstimer.chesstimer.data.di.repositoryModule
import com.example.chesstimer.chesstimer.presentation.di.chessTimerModule
import com.example.chesstimer.datastore.dataStoreModule
import com.example.chesstimer.di.appModule
import com.example.core.database.di.databaseModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ChessTimerApp : Application() {

    // Using a SupervisorJob over here so the child scopes could fail independently without affecting one another
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChessTimerApp)
            androidLogger()

            modules(
                dataStoreModule,
                repositoryModule,
                appModule,
                chessTimerModule,
                databaseModule
            )
        }
    }
}