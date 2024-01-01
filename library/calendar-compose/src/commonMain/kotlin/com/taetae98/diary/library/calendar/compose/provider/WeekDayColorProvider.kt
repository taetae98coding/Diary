package com.taetae98.diary.library.calendar.compose.provider

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalWeekSundayColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    Color(0xFFEA5148)
}

internal val LocalWeekSaturdayColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    Color(0xFF2B7BE7)
}
