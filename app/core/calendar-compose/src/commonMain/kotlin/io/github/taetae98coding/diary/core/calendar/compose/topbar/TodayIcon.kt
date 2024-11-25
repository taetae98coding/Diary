package io.github.taetae98coding.diary.core.calendar.compose.topbar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.datetime.LocalDate

@Composable
public fun TodayIcon(
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
		var today by remember { mutableStateOf(LocalDate.todayIn()) }

		Text(
			text = today.dayOfMonth.toString(),
			fontSize = with(LocalDensity.current) { 14.dp.toSp() },
			letterSpacing = with(LocalDensity.current) { 0.dp.toSp() },
			textAlign = TextAlign.Center,
			lineHeight = with(LocalDensity.current) { 14.dp.toSp() },
		)

		LifecycleResumeEffect(Unit) {
			today = LocalDate.todayIn()
			onPauseOrDispose { }
		}
	}
}
