package io.github.taetae98coding.diary.compose.calendar

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import io.github.taetae98coding.diary.compose.calendar.theme.CalendarColors
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

public data object CalendarDefaults {
    @Composable
    public fun colors(
        weekdayColor: Color = Color.Unspecified,
        weekdayVariantColor: Color = Color.Unspecified,
        saturdayColor: Color = Color.Unspecified,
        saturdayVariantColor: Color = Color.Unspecified,
        sundayColor: Color = Color.Unspecified,
        sundayVariantColor: Color = Color.Unspecified,
        primaryColor: Color = Color.Unspecified,
        selectedColor: Color = Color.Unspecified,
    ): CalendarColors {
        return CalendarColors(
            weekdayColor = weekdayColor.takeOrElse { if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF3B3B3B) },
            weekdayVariantColor = weekdayVariantColor.takeOrElse { if (isSystemInDarkTheme()) Color(0xFF888888) else Color(0xFF909090) },
            saturdayColor = saturdayColor.takeOrElse { if (isSystemInDarkTheme()) Color(0xFF70A7FF) else Color(0xFF4F80FF) },
            saturdayVariantColor = saturdayVariantColor.takeOrElse { if (isSystemInDarkTheme()) Color(0xFF4F7EC0) else Color(0xFFA5BFFF) },
            sundayColor = sundayColor.takeOrElse { if (isSystemInDarkTheme()) Color(0xFFFF8787) else Color(0xFFFF6B6B) },
            sundayVariantColor = sundayVariantColor.takeOrElse { if (isSystemInDarkTheme()) Color(0xFFC85D5D) else Color(0xFFFFBABA) },
            primaryColor = primaryColor.takeOrElse { DiaryTheme.colorScheme.primary },
            selectedColor = selectedColor.takeOrElse { DiaryTheme.colorScheme.primaryContainer },
        )
    }
}
