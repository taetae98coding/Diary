package io.github.taetae98coding.diary.core.calendar.compose.day

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.calendar.compose.CalendarDefaults
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarDayOfMonth(
	state: CalendarDayOfMonthState,
	primaryDateListProvider: () -> List<LocalDate>,
	holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
	modifier: Modifier = Modifier,
	colors: CalendarColors = CalendarDefaults.colors(),
) {
	val isPrimary by remember { derivedStateOf { state.localDate in primaryDateListProvider() } }
	val isHoliday by remember { derivedStateOf { holidayListProvider().any { state.localDate in it } } }

	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center,
	) {
		val color = when {
			isPrimary -> colors.onPrimaryColor
			state.dayOfWeek == DayOfWeek.SUNDAY || isHoliday -> colors.sundayColor
			state.dayOfWeek == DayOfWeek.SATURDAY -> colors.saturdayColor
			else -> colors.dayColor.takeOrElse { LocalContentColor.current }
		}.run {
			copy(
				alpha = alpha * if (isPrimary || state.isInMonth) {
					1F
				} else {
					0.38F
				},
			)
		}
		val size = with(LocalDensity.current) {
			DiaryTheme.typography.labelMedium.fontSize.toDp() + 16.dp
		}

		Box(
			modifier = Modifier.size(size)
				.run {
					if (isPrimary) {
						background(color = colors.primaryColor, shape = CircleShape)
					} else {
						this
					}
				},
			contentAlignment = Alignment.Center,
		) {
			Text(
				text = state.localDate.dayOfMonth.toString(),
				color = color,
				textAlign = TextAlign.Center,
				style = DiaryTheme.typography.labelMedium,
			)
		}
	}
}
