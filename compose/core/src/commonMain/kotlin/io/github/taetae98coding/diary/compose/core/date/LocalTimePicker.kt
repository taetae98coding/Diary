package io.github.taetae98coding.diary.compose.core.date

import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun LocalTimePicker(
    state: LocalTimePickerState,
    modifier: Modifier = Modifier,
) {
    TimePicker(
        state = state.materialState,
        modifier = modifier,
    )
}
