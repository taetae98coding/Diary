package com.taetae98.diary.feature.calendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.calendar.compose.Calendar
import com.taetae98.diary.library.calendar.compose.runtime.rememberCalendarState
import com.taetae98.diary.library.calendar.compose.CalendarState
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import kotlinx.datetime.number

@Composable
internal fun CalendarScreen(
    modifier: Modifier = Modifier,
) {
    val state = rememberCalendarState()

    DiaryScaffold(
        modifier = modifier,
        topBar = { TopBar(state = state) },
    ) {
        Calendar(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = state,
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
            Text(text = "${state.currentYear}년 ${state.currentMonth.number}월")
        }
    )
}