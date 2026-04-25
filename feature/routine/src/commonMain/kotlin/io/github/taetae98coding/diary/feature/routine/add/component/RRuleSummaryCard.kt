package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.icon.DeleteIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.RRuleDiaryByDay
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import kotlinx.datetime.DayOfWeek

@Composable
internal fun RRuleSummaryCard(
    rRule: RoutineRRule,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = rRule.toText(),
                modifier = Modifier.weight(1F),
                style = DiaryTheme.typography.bodyLarge,
            )
            IconButton(onClick = onDelete) {
                DeleteIcon()
            }
        }
    }
}

private fun RoutineRRule.toText(): String {
    val parts = buildList {
        diaryByDay.toText()?.let(::add)
        byMonthDay.toText()?.let(::add)
    }
    return parts.joinToString(separator = " · ")
}

private fun RRuleDiaryByDay.toText(): String? {
    if (days.isEmpty()) return null
    val daysText = days.sortedBy { it.isoDayNumber() }
        .joinToString(separator = ",") { it.toText() }
    val ord = ordinal ?: return daysText
    val prefix = if (ord == -1) "마지막" else "${ord}번째"
    return "$prefix $daysText"
}

private fun Set<Int>.toText(): String? {
    if (isEmpty()) return null
    val sorted = sortedWith(compareBy({ it == -1 }, { it }))
    return sorted.joinToString(separator = ",") { if (it == -1) "마지막 날" else "${it}일" }
}

private fun DayOfWeek.toText(): String = when (this) {
    DayOfWeek.MONDAY -> "월"
    DayOfWeek.TUESDAY -> "화"
    DayOfWeek.WEDNESDAY -> "수"
    DayOfWeek.THURSDAY -> "목"
    DayOfWeek.FRIDAY -> "금"
    DayOfWeek.SATURDAY -> "토"
    DayOfWeek.SUNDAY -> "일"
}

private fun DayOfWeek.isoDayNumber(): Int = when (this) {
    DayOfWeek.MONDAY -> 1
    DayOfWeek.TUESDAY -> 2
    DayOfWeek.WEDNESDAY -> 3
    DayOfWeek.THURSDAY -> 4
    DayOfWeek.FRIDAY -> 5
    DayOfWeek.SATURDAY -> 6
    DayOfWeek.SUNDAY -> 7
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        RRuleSummaryCard(
            rRule = RoutineRRule(
                diaryByDay = RRuleDiaryByDay(days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)),
            ),
            onDelete = {},
        )
    }
}
