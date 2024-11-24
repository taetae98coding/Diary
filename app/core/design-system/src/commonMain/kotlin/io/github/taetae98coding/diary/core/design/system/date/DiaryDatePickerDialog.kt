package io.github.taetae98coding.diary.core.design.system.date

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.library.datetime.toLocalDate
import io.github.taetae98coding.diary.library.datetime.toTimeInMillis
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun DiaryDatePickerDialog(
    localDate: LocalDate?,
    onConfirm: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberDatePickerState(initialSelectedDateMillis = localDate?.toTimeInMillis())

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            val isConfirmButtonEnable by remember { derivedStateOf { state.selectedDateMillis != null } }

            TextButton(
                onClick = {
                    state.selectedDateMillis?.toLocalDate()?.let { date ->
                        onConfirm(date)
                        onDismissRequest()
                    }
                },
                enabled = isConfirmButtonEnable,
            ) {
                Text(text = "선택")
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "닫기")
            }
        },
    ) {
        DatePicker(state = state)
    }
}
