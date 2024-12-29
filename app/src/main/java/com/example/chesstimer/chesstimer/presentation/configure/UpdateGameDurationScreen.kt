package com.example.chesstimer.chesstimer.presentation.configure

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chesstimer.chesstimer.presentation.configure.component.Dialog
import com.example.core.presentation.components.ChessTimerScaffold
import com.example.core.presentation.components.ChessTimerTopAppBar
import com.example.core.presentation.ui.theme.ConfigurationScreenSecondaryColor
import com.example.core.presentation.ui.theme.PrimaryColor
import com.example.core.presentation.ui.theme.SurfaceColor

@Composable
fun UpdateGameDurationScreen(
    state: SelectGameDurationState,
    onAction: (SelectGameDurationAction) -> Unit,
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.shouldShowConfirmationDialog) {
        openAlertDialog.value = state.shouldShowConfirmationDialog
    }

    if (openAlertDialog.value) {
        Dialog(
            onDismissRequest = {
                openAlertDialog.value = false
                onAction(SelectGameDurationAction.OnNewGameDurationRejected)
            },
            onConfirmation = {
                openAlertDialog.value = false
                onAction(SelectGameDurationAction.OnConfirmNewGameDuration)
            },
            dialogTitle = "End On going game",
            dialogText = "Are tou sure that you want to close the on going game and reset the timer"
        )
    }

    ChessTimerScaffold(
        topAppBar = {
            ChessTimerTopAppBar(
                title = "Time Configuration",
                hasBackButton = true,
                onBackButtonClicked = {
                    onBackButtonClicked()
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceColor)
                .padding(paddingValues)
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Presets",
                fontSize = 12.sp,
                color = ConfigurationScreenSecondaryColor,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            GameDurationList(
                state = state,
                onItemSelected = { selectedIndex ->
                    onAction(SelectGameDurationAction.OnDurationSelected(selectedIndex))
                },
                modifier = Modifier
                    .weight(1f) // Take all remaining space
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    onAction(SelectGameDurationAction.OnStartButtonClicked)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 12.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Start",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun GameDurationList(
    state: SelectGameDurationState,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = state.selectedDurationPosition) {
        selectedIndex = state.selectedDurationPosition
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(state.durations) { index, item ->
            GameDurationListItem(
                item = item,
                index == selectedIndex,
                onItemSelected = {
                    selectedIndex = index
                    onItemSelected(index)
                },
                modifier
            )
        }
    }
}

@Composable
fun GameDurationListItem(
    item: String,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onItemSelected()
            }
            .padding(vertical = 12.dp, horizontal = 12.dp)
    ) {
        Text(
            text = item,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        RadioButton(
            selected = isSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryColor
            ),
            onClick = null,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdateGameDurationScreenPreview() {
    UpdateGameDurationScreen(
        state = SelectGameDurationState(
            listOf(
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
                "1 min",
                "2 min",
            )
        ),
        onAction = {

        },
        onBackButtonClicked = {

        }
    )
}
