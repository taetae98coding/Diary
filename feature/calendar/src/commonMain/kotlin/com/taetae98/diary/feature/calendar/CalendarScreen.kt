package com.taetae98.diary.feature.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.calendar.Calendar
import com.taetae98.diary.library.compose.calendar.CalendarItem
import com.taetae98.diary.library.compose.calendar.CalendarState
import com.taetae98.diary.library.compose.calendar.model.DateRange
import com.taetae98.diary.library.kotlin.ext.getLocalDateTimeNow
import com.taetae98.diary.library.kotlin.ext.localDateNow
import com.taetae98.diary.ui.compose.icon.DropdownDownIcon
import com.taetae98.diary.ui.compose.icon.DropdownUpIcon
import com.taetae98.diary.ui.compose.icon.TodayIcon
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun CalendarScreen(
    modifier: Modifier = Modifier,
    state: CalendarState,
    schedule: State<ImmutableList<CalendarItem.Schedule>>,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
    onItem: (key: Any) -> Unit,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = { TopBar(state = state) },
    ) {
        Calendar(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = state,
            primaryDate = remember {
                val now = localDateNow()
                mutableStateOf(persistentListOf(DateRange(now, now)))
            },
            schedule = schedule,
            holiday = holiday,
            onItem = onItem,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    state: CalendarState,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Title(state = state)
        },
        actions = {
            val coroutineScope = rememberCoroutineScope()
            val scrollToNow: () -> Unit by remember {
                mutableStateOf({
                    coroutineScope.launch {
                        val now = getLocalDateTimeNow()
                        state.scrollTo(now.year, now.month)
                    }
                })
            }

            IconButton(
                onClick = scrollToNow
            ) {
                TodayIcon()
            }
        }
    )
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    state: CalendarState,
) {
    val coroutineScope = rememberCoroutineScope()
    var isDatePickerVisible by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier.clickable { isDatePickerVisible = !isDatePickerVisible },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "${state.currentYear}년 ${state.currentMonth.number}월")

        if (isDatePickerVisible) {
            DropdownUpIcon()
        } else {
            DropdownDownIcon()
        }
    }

    if (isDatePickerVisible) {
        val current = remember {
            LocalDate(state.currentYear, state.currentMonth, 1)
                .atStartOfDayIn(TimeZone.UTC)
                .toEpochMilliseconds()
        }

        val selectDate: (Long) -> Unit by remember {
            mutableStateOf({
                coroutineScope.launch {
                    val dateTime = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC)

                    isDatePickerVisible = false
                    state.scrollTo(dateTime.year, dateTime.month)
                }
            })
        }

        MonthPicker(
            initialDate = current,
            onDismissRequest = { isDatePickerVisible = false },
            onSelect = selectDate,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonthPicker(
    modifier: Modifier = Modifier,
    initialDate: Long,
    onDismissRequest: () -> Unit,
    onSelect: (Long) -> Unit,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = { datePickerState.selectedDateMillis?.let(onSelect) },
            ) {
                Text(text = "선택")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(text = "취소")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}