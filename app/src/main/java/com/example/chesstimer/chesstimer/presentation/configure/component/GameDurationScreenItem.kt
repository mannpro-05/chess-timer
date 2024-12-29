package com.example.chesstimer.chesstimer.presentation.configure.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.presentation.ui.theme.PrimaryColor

@Composable
fun SelectGameDurationItem(
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

@Composable
fun EditGameDurationItem(
    item: String,
    isChecked: Boolean,
    onItemChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onItemChecked(!isChecked)
            }
            .padding(start = 12.dp)
    ) {
        Text(
            text = item,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = { checked ->
                onItemChecked(checked)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryColor
            ),
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
