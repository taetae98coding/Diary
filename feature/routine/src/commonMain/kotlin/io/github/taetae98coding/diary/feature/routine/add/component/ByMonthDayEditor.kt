package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.chip.DiaryFilterChip
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
internal fun ByMonthDayEditor(
    state: ByMonthDayEditorState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "일자 (byMonthDay)",
                style = DiaryTheme.typography.titleSmall,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 7,
            ) {
                repeat(31) { index ->
                    val day = index + 1
                    val isSelected = day in state.byMonthDay

                    DiaryFilterChip(
                        selected = isSelected,
                        onClick = { if (isSelected) state.unselectDay(day) else state.selectDay(day) },
                        label = { Text(text = "$day") },
                    )
                }

                DiaryFilterChip(
                    selected = state.hasLastDay,
                    onClick = { if (state.hasLastDay) state.unselectLastDay() else state.selectLastDay() },
                    label = { Text(text = "마지막 날") },
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        ByMonthDayEditor(state = ByMonthDayEditorState(initialByMonthDay = setOf(1, 15)))
    }
}
