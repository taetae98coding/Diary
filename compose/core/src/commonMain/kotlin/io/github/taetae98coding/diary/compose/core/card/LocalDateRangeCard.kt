package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.dialog.LocalDatePickerDialogHost
import io.github.taetae98coding.diary.compose.core.dialog.rememberDialogState
import io.github.taetae98coding.diary.compose.core.icon.ClearIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.todayIn

public const val LOCAL_DATE_RANGE_CARD_START_CLEAR_TEST_TAG: String = "LocalDateRangeCardStartClear"
public const val LOCAL_DATE_RANGE_CARD_END_CLEAR_TEST_TAG: String = "LocalDateRangeCardEndClear"

@Composable
public fun LocalDateRangeCard(
    modifier: Modifier = Modifier,
    state: LocalDateRangeCardState = rememberLocalDateRangeCardState(),
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "날짜 범위",
                style = DiaryTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LocalDateField(
                    label = "시작일",
                    clearTestTag = LOCAL_DATE_RANGE_CARD_START_CLEAR_TEST_TAG,
                    localDateProvider = state::start,
                    onLocalDateChange = state::updateStart,
                    modifier = Modifier.weight(1F),
                )
                LocalDateField(
                    label = "종료일",
                    clearTestTag = LOCAL_DATE_RANGE_CARD_END_CLEAR_TEST_TAG,
                    localDateProvider = state::endInclusive,
                    onLocalDateChange = state::updateEndInclusive,
                    modifier = Modifier.weight(1F),
                )
            }
        }
    }
}

@Composable
private fun LocalDateField(
    label: String,
    clearTestTag: String,
    localDateProvider: () -> LocalDate?,
    onLocalDateChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dialogState = rememberDialogState()
    val localDate = localDateProvider()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.minimumInteractiveComponentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = DiaryTheme.typography.labelSmall,
                color = DiaryTheme.colorScheme.onSurfaceVariant,
            )
            AnimatedVisibility(visible = localDate != null) {
                IconButton(
                    onClick = { onLocalDateChange(null) },
                    modifier = Modifier.testTag(clearTestTag),
                ) {
                    ClearIcon()
                }
            }
        }

        TextButton(
            onClick = dialogState::show,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AnimatedContent(
                targetState = localDate,
                transitionSpec = localDateTransitionSpec(),
            ) { targetDate ->
                if (targetDate == null) {
                    Text(text = "없음")
                } else {
                    val format = remember {
                        LocalDate.Format {
                            year()
                            chars(value = ". ")
                            monthNumber(padding = Padding.SPACE)
                            chars(value = ". ")
                            day(padding = Padding.SPACE)
                            chars(value = " (")
                            dayOfWeek(DayOfWeekNames(listOf("월", "화", "수", "목", "금", "토", "일")))
                            chars(value = ")")
                        }
                    }

                    Text(text = remember(targetDate, format) { targetDate.format(format) })
                }
            }
        }
    }

    LocalDatePickerDialogHost(
        state = dialogState,
        localDateProvider = { localDate ?: Clock.System.todayIn(TimeZone.currentSystemDefault()) },
        onSelect = onLocalDateChange,
    )
}

private fun localDateTransitionSpec(): AnimatedContentTransitionScope<LocalDate?>.() -> ContentTransform {
    return {
        val initial = initialState
        val target = targetState
        when {
            initial == null || target == null -> {
                fadeIn() togetherWith fadeOut()
            }

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
        }.using(SizeTransform(clip = false))
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        LocalDateRangeCard()
    }
}
