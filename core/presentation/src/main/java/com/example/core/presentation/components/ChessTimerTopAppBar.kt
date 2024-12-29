@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.ui.theme.ToolbarColor

@Composable
fun ChessTimerTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ToolbarColor
        ),
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun ToolbarClickableIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onIconClicked: () -> Unit,
    tint: Color = Color.White
) {
    IconButton(onClick = { onIconClicked() }) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = tint
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChessTimerTopAppBarPreview() {
    ChessTimerTopAppBar(
        title = "Time Controls",
        navigationIcon = {
            ToolbarClickableIcon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                onIconClicked = {
                    
                }
            )
        }
    )
}
