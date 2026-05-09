@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.taetae98coding.diary.compose.core.dialog

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.compose.core.date.LocalDatePicker
import io.github.taetae98coding.diary.compose.core.date.rememberLocalDatePickerState
import kotlinx.datetime.LocalDate

@Composable
public fun LocalDatePickerDialogHost(
    state: DialogState,
    localDateProvider: () -> LocalDate,
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isVisible) {
        val datePickerState = rememberLocalDatePickerState(initialSelectedDate = localDateProvider())

        DatePickerDialog(
            onDismissRequest = state::hide,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDate?.let(onSelect)
                        state.hide()
                    },
                ) {
                    Text(text = "확인")
                }
            },
            modifier = modifier,
        ) {
            LocalDatePicker(state = datePickerState)
        }
    }
}
