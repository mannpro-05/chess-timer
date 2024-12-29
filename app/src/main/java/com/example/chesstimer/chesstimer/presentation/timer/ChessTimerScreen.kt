package com.example.chesstimer.chesstimer.presentation.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chesstimer.R
import com.example.chesstimer.ui.theme.ActiveTimerColor
import com.example.chesstimer.ui.theme.ActiveTimerTextColor
import com.example.chesstimer.ui.theme.ConsoleColor
import com.example.chesstimer.ui.theme.ConsoleIconColor
import com.example.chesstimer.ui.theme.InActiveTimerColor
import com.example.chesstimer.ui.theme.InActiveTimerTextColor
import com.example.chesstimer.ui.theme.TimeoutReachedColor

@Composable
fun ChessTimerScreen(
    state: ChessTimerState,
    onAction: (ChessTimerAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    ) {
        TimerSection(
            state,
            onAction = { action ->
                onAction(action)
            },
            onChangeTimeIconClicked = {
                onAction(ChessTimerAction.OnSelectGameDurationTimerClicked)
            }
        )
    }
}

@Composable
fun TimerSection(
    state: ChessTimerState,
    onAction: (ChessTimerAction) -> Unit,
    onChangeTimeIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Timer(
            time = state.playerOneRemainingTime,
            onTimerClicked = {
                onAction(ChessTimerAction.OnPlayerOneTimerClicked)
            },
            isTimerActive = state.isPlayerOneTimerActive,
            isTimeOutReached = state.isPlayerOneTimeOutReached,
            isFlipped = true,
            modifier = Modifier.weight(1f)
        )
        Console(
            isTimerRunning = state.isTimerRunning,
            onResetTimeIconClicked = {
                onAction(ChessTimerAction.OnRestartGamePlayClicked)
            },
            onChangeTimeIconClicked = {
                onChangeTimeIconClicked()
            },
            onPausePlayIconClicked = {
                onAction(ChessTimerAction.OnPlayPausedButtonClicked)
            }
        )
        Timer(
            time = state.playerTwoRemainingTime,
            onTimerClicked = {
                onAction(ChessTimerAction.OnPlayerTwoTimerClicked)
            },
            isTimerActive = state.isPlayerTwoTimerActive,
            isTimeOutReached = state.isPlayerTwoTimeOutReached,
            isFlipped = false,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Console(
    isTimerRunning: Boolean,
    onResetTimeIconClicked: () -> Unit,
    onChangeTimeIconClicked: () -> Unit,
    onPausePlayIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(ConsoleColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        ConsoleIcon(
            image = painterResource(id = R.drawable.restart_timer_icn),
            onClick = {
                onResetTimeIconClicked()
            }
        )
        ConsoleIcon(
            image = painterResource(id = if (isTimerRunning) R.drawable.pause_icn else R.drawable.resume_timer_icn),
            onClick = {
                onPausePlayIconClicked()
            }
        )
        ConsoleIcon(
            image = painterResource(id = R.drawable.change_time_icn),
            onClick = {
                onChangeTimeIconClicked()
            }
        )
    }
}

@Composable
fun ConsoleIcon(image: Painter, onClick: () -> Unit) {
    IconButton(
        onClick = {
            onClick()
        },
        content = {
            Icon(
                painter = image,
                contentDescription = null,
                tint = ConsoleIconColor,
                modifier = Modifier.size(30.dp)
            )
        }
    )
}

@Composable
fun Timer(
    time: String,
    onTimerClicked: () -> Unit,
    isTimerActive: Boolean,
    isTimeOutReached: Boolean,
    isFlipped: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isTimeOutReached) {
        TimeoutReachedColor
    } else if (isTimerActive) {
        ActiveTimerColor
    } else {
        InActiveTimerColor
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationZ = if (isFlipped) 180f else 0f
            }
            .background(backgroundColor)
            .clickable {
                onTimerClicked()
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isTimerActive) ActiveTimerTextColor else InActiveTimerTextColor,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConsolePreview() {
    Console(
        isTimerRunning = false,
        onResetTimeIconClicked = {

        },
        onChangeTimeIconClicked = {

        },
        onPausePlayIconClicked = {

        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ChessTimerScreenPreview() {
    ChessTimerScreen(state = ChessTimerState(
        "9:35",
        "3:45",
        isPlayerOneTimerActive = true,
        isPlayerTwoTimerActive = false
    ),
        onAction = {}
    )
}