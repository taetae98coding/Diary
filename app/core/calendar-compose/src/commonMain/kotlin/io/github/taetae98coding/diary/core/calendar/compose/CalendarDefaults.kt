package io.github.taetae98coding.diary.core.calendar.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

public data object CalendarDefaults {
	@Composable
	public fun colors(): CalendarColors =
		CalendarColors(
			sundayColor = Color(0xFFFF5F56),
			saturdayColor = Color(0xFF2133FF),
			dayColor = Color.Unspecified,
			primaryColor = DiaryTheme.color.primary,
			onPrimaryColor = DiaryTheme.color.onPrimary,
			selectColor = Color.Unspecified,
		)
}
