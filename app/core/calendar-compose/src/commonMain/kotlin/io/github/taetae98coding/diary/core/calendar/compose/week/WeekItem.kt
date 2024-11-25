package io.github.taetae98coding.diary.core.calendar.compose.week

import androidx.compose.ui.graphics.Color

internal sealed class WeekItem {
	abstract val weight: Float

	data class Space(override val weight: Float) : WeekItem()

	data class Holiday(val key: Any, val name: String, override val weight: Float) : WeekItem()

	data class Text(val key: Any, val name: String, val color: Color, override val weight: Float) : WeekItem()
}
