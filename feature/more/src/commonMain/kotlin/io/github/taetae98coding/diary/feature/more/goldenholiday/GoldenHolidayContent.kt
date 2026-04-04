package io.github.taetae98coding.diary.feature.more.goldenholiday

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.calendar.CalendarDefaults
import io.github.taetae98coding.diary.compose.calendar.ext.toSundayBasedNumber
import io.github.taetae98coding.diary.compose.calendar.item.CalendarText
import io.github.taetae98coding.diary.compose.calendar.rememberCalendarSelectState
import io.github.taetae98coding.diary.compose.calendar.week.CalendarWeekOfMonth
import io.github.taetae98coding.diary.compose.core.box.LoadingBox
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.holiday.GoldenHoliday
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.minus
import kotlinx.datetime.number

@Composable
internal fun GoldenHolidayContent(
    sortOrderProvider: () -> GoldenHolidaySortOrder,
    uiStateProvider: () -> GoldenHolidayScaffoldUiState,
    onRetry: () -> Unit,
    onLocalDateRangeSelect: (LocalDateRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = uiStateProvider(),
        modifier = modifier.fillMaxSize(),
    ) { uiState ->
        when (uiState) {
            is GoldenHolidayScaffoldUiState.Idle -> Unit

            is GoldenHolidayScaffoldUiState.Loading -> {
                LoadingBox(modifier = Modifier.fillMaxSize())
            }

            is GoldenHolidayScaffoldUiState.HolidayNotExist -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "공휴일을 불러올 수 없습니다",
                        style = DiaryTheme.typography.titleMedium,
                    )
                }
            }

            is GoldenHolidayScaffoldUiState.State -> {
                val sortOrder = sortOrderProvider()
                val sortedList = remember(uiState, sortOrder) {
                    when (sortOrder) {
                        GoldenHolidaySortOrder.LONGEST_FIRST -> uiState.goldenHolidayList.sortedByDescending { it.totalDays }
                        GoldenHolidaySortOrder.START_DATE -> uiState.goldenHolidayList.sortedBy { it.localDateRange.start }
                    }
                }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(300.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(items = sortedList) { goldenHolidayUiState ->
                        GoldenHolidayCard(
                            uiState = goldenHolidayUiState,
                            onLocalDateRangeSelect = onLocalDateRangeSelect,
                        )
                    }
                }
            }

            is GoldenHolidayScaffoldUiState.UnknownError -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "네트워크 에러입니다",
                            style = DiaryTheme.typography.titleMedium,
                        )
                        Button(onClick = onRetry) {
                            Text(text = "재시도")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoldenHolidayCard(
    uiState: GoldenHoliday,
    onLocalDateRangeSelect: (LocalDateRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    val calendarColors = CalendarDefaults.colors()
    val selectState = rememberCalendarSelectState()
    val weekOfMonthList = remember(uiState) {
        val yearMonthWeekOfMonthFirstLocalDate = uiState.yearMonth.firstDay.minus(uiState.yearMonth.firstDay.dayOfWeek.toSundayBasedNumber(), DateTimeUnit.DAY)

        uiState.localDateRange.asSequence()
            .map {
                val weekOfMonthFirstLocalDate = it.minus(it.dayOfWeek.toSundayBasedNumber(), DateTimeUnit.DAY)
                ((weekOfMonthFirstLocalDate.toEpochDays() - yearMonthWeekOfMonthFirstLocalDate.toEpochDays()) / 7).toInt()
            }
            .distinct()
            .sorted()
            .toList()
    }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${uiState.yearMonth.month.number}월(${uiState.totalDays}일)",
                style = DiaryTheme.typography.titleLarge,
            )
            Text(
                text = "${uiState.localDateRange.start} - ${uiState.localDateRange.endInclusive}",
                style = DiaryTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
                    .goldenHolidayCalendarSelect(
                        yearMonth = uiState.yearMonth,
                        weekOfMonthList = weekOfMonthList,
                        selectState = selectState,
                        onSelect = onLocalDateRangeSelect,
                    ),
            ) {
                weekOfMonthList.forEach { weekOfMonth ->
                    CalendarWeekOfMonth(
                        yearMonth = uiState.yearMonth,
                        weekOfMonth = weekOfMonth,
                        modifier = Modifier.fillMaxWidth()
                            .height(80.dp),
                        selectState = selectState,
                        holidayListProvider = { uiState.holiday.map { it.localDateRange } },
                    ) {
                        items(
                            items = uiState.holiday + uiState.annualLeave,
                            localDateRange = {
                                when (it) {
                                    is Holiday -> it.localDateRange
                                    is LocalDateRange -> it
                                    else -> error("Not support type : $it")
                                }
                            },
                        ) {
                            when (it) {
                                is Holiday -> {
                                    CalendarText(
                                        text = it.name,
                                        color = calendarColors.sundayColor,
                                    )
                                }

                                is LocalDateRange -> {
                                    CalendarText(
                                        text = "연차",
                                        color = DiaryTheme.colorScheme.primary,
                                    )
                                }

                                else -> error("Not support type : $it")
                            }
                        }
                    }
                }
            }
        }
    }
}
