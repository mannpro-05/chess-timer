package com.example.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChessTimerScaffold(
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topAppBar,
        modifier = modifier
    ) { paddingValues ->
        content(paddingValues)
    }
}
