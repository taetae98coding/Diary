package io.github.taetae98coding.diary.feature.routine.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.chip.DiaryAssistChip
import io.github.taetae98coding.diary.compose.core.dialog.LocalDatePickerDialogHost
import io.github.taetae98coding.diary.compose.core.dialog.rememberDialogState
import io.github.taetae98coding.diary.compose.core.icon.AddIcon
import io.github.taetae98coding.diary.compose.core.icon.ClearIcon
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.todayIn

@Composable
internal fun RoutineDateListCard(
    title: String,
    dates: List<LocalDate>,
    onAdd: (LocalDate) -> Unit,
    onRemove: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dialogState = rememberDialogState()
    val format = remember {
        LocalDate.Format {
            year()
            chars(value = ". ")
            monthNumber(padding = Padding.SPACE)
            chars(value = ". ")
            day(padding = Padding.SPACE)
        }
    }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = DiaryTheme.typography.titleMedium,
            )

            if (dates.isNotEmpty()) {
                FlexBox(
                    config = {
                        wrap(FlexWrap.Wrap)
                        columnGap(8.dp)
                        rowGap(4.dp)
                    },
                ) {
                    dates.forEach { date ->
                        DiaryAssistChip(
                            onClick = { onRemove(date) },
                            label = { Text(text = remember(date) { date.format(format) }) },
                            trailingIcon = { ClearIcon() },
                        )
                    }
                }
            }

            FilledTonalButton(onClick = dialogState::show) {
                AddIcon()
                Text(text = "추가")
            }
        }
    }

    LocalDatePickerDialogHost(
        state = dialogState,
        localDateProvider = { Clock.System.todayIn(TimeZone.currentSystemDefault()) },
        onSelect = onAdd,
    )
}
