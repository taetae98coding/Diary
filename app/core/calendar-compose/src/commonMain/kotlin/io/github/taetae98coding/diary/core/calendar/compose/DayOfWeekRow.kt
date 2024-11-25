package io.github.taetae98coding.diary.core.calendar.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.style.TextAlign
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@Composable
internal fun DayOfWeekRow(
	modifier: Modifier = Modifier,
	colors: CalendarColors = CalendarDefaults.colors(),
) {
	Row(modifier = modifier) {
		val dayOfWeekModifier = Modifier.weight(1F)

		Text(
			text = "일",
			modifier = dayOfWeekModifier,
			color = colors.sundayColor,
			textAlign = TextAlign.Center,
			style = DiaryTheme.typography.bodySmall,
		)
		Text(
			text = "월",
			modifier = dayOfWeekModifier,
			color = colors.dayColor.takeOrElse { LocalContentColor.current },
			textAlign = TextAlign.Center,
			style = DiaryTheme.typography.bodySmall,
		)
		Text(
			text = "화",
			modifier = dayOfWeekModifier,
			color = colors.dayColor.takeOrElse { LocalContentColor.current },
			textAlign = TextAlign.Center,
			style = DiaryTheme.typography.bodySmall,
		)
		Text(
			text = "수",
			modifier = dayOfWeekModifier,
			color = colors.dayColor.takeOrElse { LocalContentColor.current },
			textAlign = TextAlign.Center,
			style = DiaryTheme.typography.bodySmall,
		)
		Text(
			text = "목",
			modifier = dayOfWeekModifier,
			color = colors.dayColor.takeOrElse { LocalContentColor.current },
			textAlign = TextAlign.Center,
			style = DiaryTheme.typography.bodySmall,
		)
		Text(
			text = "금",
			modifier = dayOfWeekModifier,
			color = colors.dayColor.takeOrElse { LocalContentColor.current },
			textAlign = TextAlign.Center,
			style = DiaryTheme.typography.bodySmall,
		)
		Text(
			text = "토",
			modifier = dayOfWeekModifier,
			color = colors.saturdayColor,
			textAlign = TextAlign.Center,
			style = DiaryTheme.typography.bodySmall,
		)
	}
}
