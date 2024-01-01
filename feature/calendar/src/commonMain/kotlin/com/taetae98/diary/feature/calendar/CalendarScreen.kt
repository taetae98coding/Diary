package com.taetae98.diary.feature.calendar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.calendar.compose.Calendar
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold

@Composable
internal fun CalendarScreen(
    modifier: Modifier = Modifier,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = { TopBar() },
    ) {
        Calendar(
            modifier = Modifier.padding(it)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "캘린더") }
    )
}