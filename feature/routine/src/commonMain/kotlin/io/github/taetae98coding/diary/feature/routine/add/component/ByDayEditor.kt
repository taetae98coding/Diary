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
import io.github.taetae98coding.diary.core.model.routine.RRuleDiaryByDay
import kotlinx.datetime.DayOfWeek

private val ORDINAL_LIST = listOf(null, 1, 2, 3, 4, 5)

@Composable
internal fun ByDayEditor(
    state: ByDayEditorState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "요일 (byDay)",
                style = DiaryTheme.typography.titleSmall,
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                DayOfWeek.entries.forEach { day ->
                    val isSelected = day in state.selectedDays

                    DiaryFilterChip(
                        selected = isSelected,
                        onClick = { if (isSelected) state.unselectDay(day) else state.selectDay(day) },
                        label = { Text(text = day.toText()) },
                    )
                }
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                ORDINAL_LIST.forEach { ordinal ->
                    DiaryFilterChip(
                        selected = state.ordinal == ordinal,
                        onClick = { state.updateOrdinal(ordinal) },
                        label = { Text(text = ordinal.toText()) },
                    )
                }
            }
        }
    }
}

private fun Int?.toText(): String = if (this == null) "매주" else "${this}번째"

private fun DayOfWeek.toText(): String = when (this) {
    DayOfWeek.MONDAY -> "월"
    DayOfWeek.TUESDAY -> "화"
    DayOfWeek.WEDNESDAY -> "수"
    DayOfWeek.THURSDAY -> "목"
    DayOfWeek.FRIDAY -> "금"
    DayOfWeek.SATURDAY -> "토"
    DayOfWeek.SUNDAY -> "일"
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        ByDayEditor(
            state = ByDayEditorState(
                initialDiaryByDay = RRuleDiaryByDay(
                    days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                    ordinal = 2,
                ),
            ),
        )
    }
}
