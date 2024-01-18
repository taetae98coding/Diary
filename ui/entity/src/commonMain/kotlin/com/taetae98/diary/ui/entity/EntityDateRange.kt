package com.taetae98.diary.ui.entity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.taetae98.diary.library.compose.color.ColorPickerDialog
import com.taetae98.diary.library.kotlin.ext.toLocalDate
import com.taetae98.diary.ui.compose.switch.TextSwitch

@Composable
public fun EntityDateRange(
    modifier: Modifier = Modifier,
    uiState: State<DateRangeUiState>,
) {
    Card(
        modifier = modifier,
    ) {
        Content(uiState = uiState)
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: State<DateRangeUiState>,
) {
    Column(modifier = modifier) {
        TextSwitch(
            modifier = Modifier.fillMaxWidth(),
            text = "캘린더",
            value = uiState.value.hasDate,
            onValueChange = uiState.value.onHasDateChange,
        )

        InformationLayout(uiState = uiState)
    }
}

@Composable
private fun ColumnScope.InformationLayout(
    modifier: Modifier = Modifier,
    uiState: State<DateRangeUiState>,
) {
    AnimatedVisibility(uiState.value.hasDate) {
        Column(modifier = modifier) {
            ColorLayout(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState,
            )
            DateLayout(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState,
            )
        }
    }
}

@Composable
private fun ColorLayout(
    modifier: Modifier = Modifier,
    uiState: State<DateRangeUiState>,
) {
    val isDialogVisible = rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier.clickable {
            isDialogVisible.value = true
        }.padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "컬러")

        Box(
            modifier = Modifier.size(32.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape,
                )
                .drawBehind {
                    drawCircle(Color(uiState.value.color))
                }
        )
    }

    ColorDialogWrapper(
        modifier = modifier,
        uiState = uiState,
        isDialogVisible = isDialogVisible,
    )
}

@Composable
private fun ColorDialogWrapper(
    modifier: Modifier = Modifier,
    uiState: State<DateRangeUiState>,
    isDialogVisible: MutableState<Boolean>,
) {
    if (isDialogVisible.value) {
        ColorPickerDialog(
            modifier = modifier,
            color = Color(uiState.value.color),
            onSelect = {
                uiState.value.onColorChange(it.toArgb().toLong())
                isDialogVisible.value = false
            },
            onDismissRequest = { isDialogVisible.value = false }
        )
    }
}

@Composable
private fun DateLayout(
    modifier: Modifier = Modifier,
    uiState: State<DateRangeUiState>,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DateButton(
            date = uiState.value.start,
            setDate = uiState.value.setStart
        )
        Text(
            text = " ~ "
        )
        DateButton(
            date = uiState.value.endInclusive,
            setDate = uiState.value.setEndInclusive,
        )
    }
}

@Composable
private fun DateButton(
    modifier: Modifier = Modifier,
    date: Long,
    setDate: (Long) -> Unit,
) {
    val displayText = remember(date) { date.toLocalDate().toString() }
    val isDialogVisible = rememberSaveable { mutableStateOf(false) }

    TextButton(
        modifier = modifier,
        onClick = { isDialogVisible.value = true }
    ) {
        Text(
            text = displayText,
        )
    }

    DateDialogWrapper(
        date = date,
        setDate = setDate,
        isDialogVisible = isDialogVisible,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateDialogWrapper(
    modifier: Modifier = Modifier,
    date: Long,
    setDate: (Long) -> Unit,
    isDialogVisible: MutableState<Boolean>,
) {
    if (isDialogVisible.value) {
        val state = rememberDatePickerState(initialSelectedDateMillis = date)

        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = { isDialogVisible.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let(setDate)
                        isDialogVisible.value = false
                    }
                ) {
                    Text(text = "선택")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isDialogVisible.value = false }
                ) {
                    Text(text = "취소")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
