package io.github.taetae98coding.diary.library.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.random.Random
import kotlin.random.nextLong

public fun Color.multiplyAlpha(value: Float): Color = copy(alpha * value)

public fun Color.toContrastColor(): Color =
	if (getColorContrast(Color.Black, this) >= getColorContrast(Color.White, this) + 5) {
		Color.Black
	} else {
		Color.White
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

public fun randomArgb(): Int = (Random.nextLong(0x000000L..0xFFFFFFL) + 0xFF000000L).toInt()

@OptIn(ExperimentalStdlibApi::class)
public fun Int.toRgbString(): String = runCatching { toHexString(HexFormat.UpperCase).substring(2..7) }.getOrNull().orEmpty()
