package io.github.taetae98coding.diary.core.design.system.color

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.library.color.randomArgb

public class DiaryColorPickerState internal constructor(initialColor: Color) {
	public var color: Color by mutableStateOf(initialColor)
		private set

	internal val red by derivedStateOf { this.color.red }
	internal val green by derivedStateOf { this.color.green }
	internal val blue by derivedStateOf { this.color.blue }

	internal fun refresh() {
		color = Color(randomArgb())
	}

	internal fun onRedChange(value: Float) {
		color = color.copy(red = value)
	}

	internal fun onGreenChange(value: Float) {
		color = color.copy(green = value)
	}

	internal fun onBlueChange(value: Float) {
		color = color.copy(blue = value)
	}

	public companion object {
		internal fun saver(): Saver<DiaryColorPickerState, Any> =
			listSaver(
				save = { listOf(it.color.toArgb()) },
				restore = {
					DiaryColorPickerState(initialColor = Color(it[0]))
				},
			)
	}
}
