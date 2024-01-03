package com.taetae98.diary.library.calendar.compose.week

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
internal sealed class WeekDayItem {
    abstract val weight: Float

    data class Space(
        override val weight: Float
    ) : WeekDayItem()

    data class Item(
        val key: Any,
        val name: String,
        override val weight: Float,
        val color: Color,
    ) : WeekDayItem()
}