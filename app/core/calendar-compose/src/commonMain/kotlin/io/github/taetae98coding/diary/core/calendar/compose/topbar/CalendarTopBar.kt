package io.github.taetae98coding.diary.core.calendar.compose.topbar

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import io.github.taetae98coding.diary.core.calendar.compose.state.CalendarState
import io.github.taetae98coding.diary.core.design.system.date.DiaryDatePickerDialog
import io.github.taetae98coding.diary.core.design.system.icon.DropDownIcon
import io.github.taetae98coding.diary.core.design.system.icon.DropUpIcon
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun CalendarTopBar(
    state: CalendarState,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            val coroutineScope = rememberCoroutineScope()
            var isDialogVisible by rememberSaveable { mutableStateOf(false) }

            Row(
                modifier = Modifier.clip(CircleShape)
                    .clickable { isDialogVisible = true }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "${state.localDate.year}년 ${state.localDate.monthNumber}월")
                DropIcon(dropUpProvider = { isDialogVisible })
            }

            if (isDialogVisible) {
                DiaryDatePickerDialog(
                    localDate = state.localDate,
                    onConfirm = { coroutineScope.launch { state.animateScrollTo(it) } },
                    onDismissRequest = { isDialogVisible = false },
                )
            }
        },
        modifier = modifier,
        actions = {
            val coroutineScope = rememberCoroutineScope()
            var today by remember { mutableStateOf(LocalDate.todayIn()) }

            IconButton(onClick = { coroutineScope.launch { state.animateScrollTo(LocalDate.todayIn()) } }) {
                TodayIcon(text = today.dayOfMonth.toString())
            }

            LifecycleResumeEffect(Unit) {
                today = LocalDate.todayIn()
                onPauseOrDispose { }
            }
        },
    )
}

@Composable
private fun DropIcon(
    dropUpProvider: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = dropUpProvider(),
        modifier = modifier,
    ) { isDropUp ->
        if (isDropUp) {
            DropUpIcon()
        } else {
            DropDownIcon()
        }
    }
}

@Composable
private fun TodayIcon(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(24.dp)
            .border(
                width = 1.dp,
                color = LocalContentColor.current,
                shape = RoundedCornerShape(size = 6.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontSize = with(LocalDensity.current) { 14.dp.toSp() },
            letterSpacing = with(LocalDensity.current) { 0.dp.toSp() },
            textAlign = TextAlign.Center,
            lineHeight = with(LocalDensity.current) { 14.dp.toSp() },
        )
    }
}
