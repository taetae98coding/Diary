package io.github.taetae98coding.diary.compose.core.dialog

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.compose.core.date.LocalTimePicker
import io.github.taetae98coding.diary.compose.core.date.rememberLocalTimePickerState
import kotlinx.datetime.LocalTime

@Composable
public fun LocalTimePickerDialogHost(
    state: DialogState,
    localTimeProvider: () -> LocalTime,
    onSelect: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
) {
    if (state.isVisible) {
        val datePickerState = rememberLocalTimePickerState(initialSelectedTime = localTimeProvider())

        TimePickerDialog(
            onDismissRequest = state::hide,
            confirmButton = {
                TextButton(
                    onClick = {
                        onSelect(datePickerState.selectedTime)
                        state.hide()
                    },
                ) {
                    Text(text = "확인")
                }
            },
            title = title,
            modifier = modifier,
        ) {
            LocalTimePicker(state = datePickerState)
        }
    }
}
