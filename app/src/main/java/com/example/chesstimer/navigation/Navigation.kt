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
import com.example.chesstimer.navigation.NavigationHelper.CHESS_GAME_TIMER_CONFIG_ROUTE
import com.example.chesstimer.navigation.NavigationHelper.CHESS_GAME_TIMER_ROUTE
import com.example.chesstimer.navigation.NavigationHelper.CURRENT_GAME_TIME_ARG
import com.example.chesstimer.navigation.NavigationHelper.IS_GAME_RUNNING_ARG
import com.example.core.presentation.ObserveAsEvents
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
        startDestination = CHESS_GAME_TIMER_ROUTE,
        route = "chess_game"
    ) {
        composable(route = CHESS_GAME_TIMER_ROUTE) {
            val viewModel: ChessTimerViewModel = koinViewModel()
            val state = viewModel.state.collectAsStateWithLifecycle().value

            ObserveAsEvents(events = viewModel.events) { chessTimerEvent ->
                when (chessTimerEvent) {
                    is ChessTimerEvent.LaunchSelectGameDuration -> {
                        navController.navigate("$CHESS_GAME_TIMER_CONFIG_ROUTE/${chessTimerEvent.isGameRunning}/${chessTimerEvent.currentGameTime}")
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
            route = "$CHESS_GAME_TIMER_CONFIG_ROUTE/{$IS_GAME_RUNNING_ARG}/{$CURRENT_GAME_TIME_ARG}",
            arguments = listOf(navArgument(IS_GAME_RUNNING_ARG) {
                type = NavType.BoolType
            }
            )
        ) { backStackEntry ->
            val isGameRunning = backStackEntry.arguments?.getBoolean(IS_GAME_RUNNING_ARG) ?: false
            val currentGameDuration =
                backStackEntry.arguments?.getString(CURRENT_GAME_TIME_ARG)?.toLong() ?: 0L
            val viewModel: SelectGameDurationViewModel =
                koinViewModel { parametersOf(isGameRunning, currentGameDuration) }
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
                },
                onBackButtonClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
}

object NavigationHelper {
    const val IS_GAME_RUNNING_ARG = "isGameRunning"
    const val CURRENT_GAME_TIME_ARG = "currentGameTime"
    const val APP_ENTRY_ROUTE = "chess_timer"
    const val CHESS_GAME_TIMER_ROUTE = "timer"
    const val CHESS_GAME_TIMER_CONFIG_ROUTE = "configuration"
}