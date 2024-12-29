package com.example.chesstimer.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.chesstimer.chesstimer.presentation.configure.SelectGameDurationEvent
import com.example.chesstimer.chesstimer.presentation.configure.SelectGameDurationViewModel
import com.example.chesstimer.chesstimer.presentation.configure.UpdateGameDurationScreen
import com.example.chesstimer.chesstimer.presentation.timer.ChessTimerEvent
import com.example.chesstimer.chesstimer.presentation.timer.ChessTimerScreen
import com.example.chesstimer.chesstimer.presentation.timer.ChessTimerViewModel
import com.example.chesstimer.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "chess_game"
    ) {
        appGraph(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.appGraph(navController: NavHostController) {
    navigation(
        startDestination = "timer",
        route = "chess_game"
    ) {
        composable(route = "timer") {
            val viewModel: ChessTimerViewModel = koinViewModel()
            val state = viewModel.state.collectAsStateWithLifecycle().value

            ObserveAsEvents(events = viewModel.events) { chessTimerEvent ->
                when (chessTimerEvent) {
                    is ChessTimerEvent.LaunchSelectGameDuration -> {
                        navController.navigate("configuration/${chessTimerEvent.isGameRunning}")
                    }
                }
            }

            ChessTimerScreen(
                state = state,
                onAction = {
                    viewModel.onAction(it)
                }
            )
        }

        composable(
            route = "configuration/{isGameRunning}",
            arguments = listOf(navArgument("isGameRunning") {
                type = NavType.BoolType
            }
            )
        ) { backStackEntry ->
            val isGameRunning = backStackEntry.arguments?.getBoolean("isGameRunning") ?: false
            val viewModel: SelectGameDurationViewModel =
                koinViewModel { parametersOf(isGameRunning) }
            val state = viewModel.state.collectAsStateWithLifecycle().value
            ObserveAsEvents(events = viewModel.event) { chessTimerEvent ->
                when (chessTimerEvent) {
                    SelectGameDurationEvent.GameDurationUpdated -> {
                        navController.popBackStack()
                    }
                }
            }
            UpdateGameDurationScreen(
                state = state,
                onAction = {
                    viewModel.onAction(it)
                }
            )
        }
    }
}