package io.github.taetae98coding.diary.core.calendar.compose.topbar

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.calendar.compose.state.CalendarState
import io.github.taetae98coding.diary.core.design.system.date.DiaryDatePickerDialog
import io.github.taetae98coding.diary.core.design.system.icon.DropDownIcon
import io.github.taetae98coding.diary.core.design.system.icon.DropUpIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun CalendarTopBar(
	state: CalendarState,
	actions: @Composable RowScope.() -> Unit = {},
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
		actions = actions,
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
