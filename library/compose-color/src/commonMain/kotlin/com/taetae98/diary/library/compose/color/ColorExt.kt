package com.taetae98.diary.library.compose.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

public fun Color.toContrastColor(): Color {
    return if (getColorContrast(Color.Black, this) >= getColorContrast(Color.White, this)) {
        Color.Black
    } else {
        Color.White
    }
}


private fun getColorContrast(
    color1: Color,
    color2: Color,
): Double {
    val luminance1 = color1.luminance()
    val luminance2 = color2.luminance()
    val bright = maxOf(luminance1, luminance2)
    val dark = minOf(luminance1, luminance2)

    return (bright + 0.05) / (dark + 0.05)
}