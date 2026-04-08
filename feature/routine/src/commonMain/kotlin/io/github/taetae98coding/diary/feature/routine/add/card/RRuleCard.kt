package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.chip.DiaryFilterChip
import io.github.taetae98coding.diary.compose.core.icon.RemoveIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import kotlinx.datetime.DayOfWeek

private val DAY_OF_WEEK_LABELS = listOf("월", "화", "수", "목", "금", "토", "일")
private val ORDINAL_VALUES = listOf(null, 1, 2, 3, 4, -1)

private fun Int?.toOrdinalText(): String {
    return when (this) {
        null -> "매주"
        1 -> "첫째주"
        2 -> "둘째주"
        3 -> "셋째주"
        4 -> "넷째주"
        -1 -> "마지막주"
        else -> "${this}번째주"
    }
}

@Composable
internal fun RRuleCard(
    state: RRuleCardState,
    modifier: Modifier = Modifier,
) {
    RRuleCard(
        rRulesProvider = { state.rRules },
        typeSelectorState = state.typeSelectorState,
        modifier = modifier,
        onAdd = state::addRRule,
        onRemove = state::removeRRule,
    )
}

@Composable
internal fun RRuleCard(
    modifier: Modifier = Modifier,
    rRulesProvider: () -> List<RoutineRRule> = { emptyList() },
    onAdd: (List<RoutineRRule>) -> Unit = {},
    onRemove: (RoutineRRule) -> Unit = {},
) {
    RRuleCard(
        typeSelectorState = rememberRRuleTypeSelectorState(),
        rRulesProvider = rRulesProvider,
        modifier = modifier,
        onAdd = onAdd,
        onRemove = onRemove,
    )
}

@Composable
private fun RRuleCard(
    typeSelectorState: RRuleTypeSelectorState,
    modifier: Modifier = Modifier,
    rRulesProvider: () -> List<RoutineRRule> = { emptyList() },
    onAdd: (List<RoutineRRule>) -> Unit = {},
    onRemove: (RoutineRRule) -> Unit = {},
) {
    Card(modifier = modifier) {
        val isRRuleEmpty by remember { derivedStateOf { rRulesProvider().isEmpty() } }

        Text(
            text = "반복 규칙",
            style = DiaryTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )

        RRuleColumn(
            rRulesProvider = rRulesProvider,
            onRemove = onRemove,
        )

        if (!isRRuleEmpty) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }

        AddRRuleSection(
            typeSelectorState = typeSelectorState,
            onAdd = onAdd,
        )
    }
}

@Composable
private fun RRuleColumn(
    modifier: Modifier = Modifier,
    rRulesProvider: () -> List<RoutineRRule> = { emptyList() },
    onRemove: (RoutineRRule) -> Unit = {},
) {
    Column(modifier) {
        rRulesProvider().forEach { rRule ->
            RRuleItem(
                rRule = rRule,
                onRemove = { onRemove(rRule) },
            )
        }
    }
}

@Composable
private fun RRuleItem(
    rRule: RoutineRRule,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val text = when (rRule) {
            is RoutineRRule.ByDay -> {
                val dayName = DAY_OF_WEEK_LABELS[rRule.dayOfWeek.ordinal]

                when (rRule.ordinal) {
                    null -> "매주 ${dayName}요일"
                    1 -> "매월 첫째 ${dayName}요일"
                    2 -> "매월 둘째 ${dayName}요일"
                    3 -> "매월 셋째 ${dayName}요일"
                    4 -> "매월 넷째 ${dayName}요일"
                    -1 -> "매월 마지막 ${dayName}요일"
                    else -> "매월 ${rRule.ordinal}번째 ${dayName}요일"
                }
            }

            is RoutineRRule.ByMonthDay -> "매월 ${rRule.day}일"
        }

        Text(
            text = text,
            style = DiaryTheme.typography.bodyMedium,
        )
        IconButton(onClick = onRemove) {
            RemoveIcon()
        }
    }
}

@Composable
private fun AddRRuleSection(
    typeSelectorState: RRuleTypeSelectorState,
    modifier: Modifier = Modifier,
    onAdd: (List<RoutineRRule>) -> Unit = {},
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RRuleTypeSelector(state = typeSelectorState)

        when (typeSelectorState.rRuleType) {
            RRuleType.ByDay -> ByDaySelector(onAdd = onAdd)
            RRuleType.ByMonthDay -> ByMonthDaySelector(onAdd = { onAdd(listOf(it)) })
        }
    }
}

@Composable
private fun RRuleTypeSelector(
    state: RRuleTypeSelectorState,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        RRuleType.entries.forEachIndexed { index, type ->
            SegmentedButton(
                selected = state.rRuleType == type,
                onClick = { state.updateRRuleType(type) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = RRuleType.entries.size,
                ),
            ) {
                Text(
                    text = when (type) {
                        RRuleType.ByDay -> "요일별"
                        RRuleType.ByMonthDay -> "날짜별"
                    },
                )
            }
        }
    }
}

@Composable
private fun ByDaySelector(
    onAdd: (List<RoutineRRule>) -> Unit,
    modifier: Modifier = Modifier,
    state: ByDaySelectorState = rememberByDaySelectorState(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExposedDropdownMenuBox(
            expanded = state.expanded,
            onExpandedChange = state::updateExpanded,
        ) {
            OutlinedTextField(
                value = state.selectedOrdinal.toOrdinalText(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = state.expanded,
                onDismissRequest = { state.updateExpanded(false) },
            ) {
                ORDINAL_VALUES.forEach { ordinal ->
                    DropdownMenuItem(
                        text = { Text(text = ordinal.toOrdinalText()) },
                        onClick = { state.updateSelectedOrdinal(ordinal) },
                    )
                }
            }
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        ) {
            DayOfWeek.entries.forEachIndexed { index, dayOfWeek ->
                val isSelected = dayOfWeek in state.selectedDaysOfWeek

                DiaryFilterChip(
                    selected = isSelected,
                    onClick = {
                        if (isSelected) {
                            state.removeDayOfWeek(dayOfWeek)
                        } else {
                            state.addDayOfWeek(dayOfWeek)
                        }
                    },
                    label = { Text(text = DAY_OF_WEEK_LABELS[index]) },
                )
            }
        }

        TextButton(
            onClick = {
                onAdd(state.selectedDaysOfWeek.map { RoutineRRule.ByDay(it, state.selectedOrdinal) })
                state.clear()
            },
            modifier = Modifier.align(Alignment.End),
        ) {
            Text(text = "추가")
        }
    }
}

@Composable
private fun ByMonthDaySelector(
    onAdd: (RoutineRRule) -> Unit,
    modifier: Modifier = Modifier,
    state: ByMonthDaySelectorState = rememberByMonthDaySelectorState(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExposedDropdownMenuBox(
            expanded = state.expanded,
            onExpandedChange = state::updateExpanded,
        ) {
            OutlinedTextField(
                value = "${state.day}일",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = state.expanded,
                onDismissRequest = { state.updateExpanded(false) },
            ) {
                (1..31).forEach { d ->
                    DropdownMenuItem(
                        text = { Text(text = "${d}일") },
                        onClick = { state.updateDay(d) },
                    )
                }
            }
        }

        TextButton(
            onClick = { onAdd(RoutineRRule.ByMonthDay(day = state.day)) },
            modifier = Modifier.align(Alignment.End),
        ) {
            Text(text = "추가")
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        RRuleCard()
    }
}

@ComponentPreview
@Composable
private fun RRulesPreview() {
    DiaryTheme {
        RRuleCard(
            rRulesProvider = {
                listOf(
                    RoutineRRule.ByDay(DayOfWeek.MONDAY, null),
                    RoutineRRule.ByDay(DayOfWeek.WEDNESDAY, null),
                    RoutineRRule.ByMonthDay(15),
                )
            },
        )
    }
}
