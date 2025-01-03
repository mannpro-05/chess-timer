package com.example.chesstimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.chesstimer.navigation.NavigationRoot
import com.example.core.presentation.ui.theme.ChessTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChessTimerTheme {
                val navController = rememberNavController()
                NavigationRoot(
                    navController = navController
                )
            }
        }
    }
}