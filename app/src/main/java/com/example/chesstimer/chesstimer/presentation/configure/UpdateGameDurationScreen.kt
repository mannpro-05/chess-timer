package com.example.chesstimer.chesstimer.presentation.configure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chesstimer.chesstimer.presentation.configure.component.Dialog
import com.example.chesstimer.chesstimer.presentation.configure.component.EditGameDurationItem
import com.example.chesstimer.chesstimer.presentation.configure.component.SelectGameDurationItem
import com.example.core.presentation.components.ChessTimerScaffold
import com.example.core.presentation.components.ChessTimerTopAppBar
import com.example.core.presentation.components.ToolbarClickableIcon
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
                navigationIcon = {
                    ToolbarClickableIcon(
                        imageVector = if (state.isInEditMode) Icons.Default.Check else Icons.AutoMirrored.Default.ArrowBack,
                        onIconClicked = {
                            if (state.isInEditMode) {

                            } else {
                                onBackButtonClicked()
                            }
                        }
                    )
                },
                actions = {
                    if (state.isInEditMode) {
                        ToolbarClickableIcon(
                            imageVector = Icons.Outlined.Delete,
                            onIconClicked = {
                                onAction(SelectGameDurationAction.OnEditButtonClicked)
                            }
                        )
                    } else {
                        ToolbarClickableIcon(
                            imageVector = Icons.Outlined.Edit,
                            onIconClicked = {
                                onAction(SelectGameDurationAction.OnEditButtonClicked)
                            }
                        )

                        ToolbarClickableIcon(
                            imageVector = Icons.Outlined.Settings,
                            onIconClicked = {
                                onBackButtonClicked()
                            }
                        )
                    }
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
                onItemChecked = { itemPosition, isChecked ->
                    onAction(
                        SelectGameDurationAction.OnEditItemChecked(
                            itemPosition = itemPosition,
                            isChecked = isChecked
                        )
                    )
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
    onItemChecked: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isInEditMode) {
        EditGameDurationList(
            editableItems = state.editableItems,
            onItemChecked = { position, isChecked ->
                onItemChecked(position, isChecked)
            },
            modifier = modifier
        )
    } else {
        SelectGameDurationList(
            durations = state.durations,
            selectedDurationPosition = state.selectedDurationPosition,
            onItemSelected = { position ->
                onItemSelected(position)
            },
            modifier = modifier
        )
    }
}

@Composable
fun SelectGameDurationList(
    durations: List<String>,
    selectedDurationPosition: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = selectedDurationPosition) {
        selectedIndex = selectedDurationPosition
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(durations) { index, item ->
            SelectGameDurationItem(
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
fun EditGameDurationList(
    editableItems: List<EditableItem>,
    onItemChecked: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(editableItems) { index, item ->
            EditGameDurationItem(
                item = item.item,
                isChecked = item.isSelected,
                onItemChecked = { isChecked ->
                    onItemChecked(index, isChecked)
                }
            )
        }
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
            ),
            isInEditMode = false
        ),
        onAction = {

        },
        onBackButtonClicked = {

        }
    )
}
