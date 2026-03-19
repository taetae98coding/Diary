package io.github.taetae98coding.diary.compose.core.date

import androidx.compose.material3.DateRangePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun DiaryDateRangePicker(
    state: DiaryDateRangePickerState,
    modifier: Modifier = Modifier,
) {
    DateRangePicker(
        state = state.materialState,
        modifier = modifier,
    )
}
