package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.dialog.LocalDatePickerDialogHost
import io.github.taetae98coding.diary.compose.core.dialog.rememberDialogState
import io.github.taetae98coding.diary.compose.core.icon.ClearIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.Padding
import kotlinx.datetime.todayIn

@Composable
internal fun RoutineDateRangeCard(
    state: RoutineDateRangeCardState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Text(
            text = "활성 기간",
            style = DiaryTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
        DateRow(
            label = "시작일",
            localDate = state.start,
            onDateSelect = state::updateStart,
            onClear = { state.updateStart(null) },
        )
        DateRow(
            label = "종료일",
            localDate = state.endInclusive,
            onDateSelect = state::updateEndInclusive,
            onClear = { state.updateEndInclusive(null) },
        )
    }
}

@Composable
private fun DateRow(
    label: String,
    localDate: LocalDate?,
    onDateSelect: (LocalDate) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dialogState = rememberDialogState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = DiaryTheme.typography.bodyMedium,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = dialogState::show) {
                AnimatedContent(
                    targetState = localDate,
                    transitionSpec = transitionSpec(),
                ) { localDate ->
                    val format = remember {
                        LocalDate.Format {
                            year()
                            chars(".")
                            monthNumber(padding = Padding.ZERO)
                            chars(".")
                            day(padding = Padding.ZERO)
                        }
                    }

                    Text(text = remember(localDate, format) { localDate?.let { format.format(it) } } ?: "$label 없음")
                }
            }
            AnimatedVisibility(localDate != null) {
                IconButton(onClick = onClear) {
                    ClearIcon()
                }
            }
        }
    }

    LocalDatePickerDialogHost(
        state = dialogState,
        localDateProvider = { localDate ?: Clock.System.todayIn(TimeZone.currentSystemDefault()) },
        onSelect = onDateSelect,
    )
}

private fun transitionSpec(): AnimatedContentTransitionScope<LocalDate?>.() -> ContentTransform {
    return {
        val target = targetState
        val initial = initialState

        when {
            target == null || initial == null -> EnterTransition.None togetherWith ExitTransition.None

            target > initial -> {
                val inAnimation = slideInVertically { height -> height } + fadeIn()
                val outAnimation = slideOutVertically { height -> -height } + fadeOut()

                inAnimation togetherWith outAnimation
            }

            else -> {
                val inAnimation = slideInVertically { height -> -height } + fadeIn()
                val outAnimation = slideOutVertically { height -> height } + fadeOut()

                inAnimation togetherWith outAnimation
            }
        }.using(
            SizeTransform(clip = false),
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        RoutineDateRangeCard(state = rememberRoutineDateRangeCardState())
    }
}

@ComponentPreview
@Composable
private fun DatePreview() {
    DiaryTheme {
        RoutineDateRangeCard(
            state = rememberRoutineDateRangeCardState(
                initialStart = LocalDate(1998, 1, 9),
                initialEndInclusive = LocalDate(1998, 12, 31),
            ),
        )
    }
}
