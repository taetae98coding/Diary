package com.taetae98.diary.library.compose.color

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

public val LocalRedDividerColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    Color(0xFFFF5F56)
}

public val LocalGreenDividerColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    Color(0xFF5AA55A)
}

public val LocalBlueDividerColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    Color(0xFF2133FF)
}