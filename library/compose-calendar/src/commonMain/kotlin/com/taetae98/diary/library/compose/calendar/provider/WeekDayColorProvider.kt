package com.taetae98.diary.library.compose.calendar.provider

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalWeekSundayColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    Color(0xFFFF5F56)
}

internal val LocalWeekSaturdayColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    Color(0xFF2133FF)
}
