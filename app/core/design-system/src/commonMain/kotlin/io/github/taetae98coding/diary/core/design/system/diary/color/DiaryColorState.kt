package io.github.taetae98coding.diary.core.design.system.diary.color

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

public class DiaryColorState(initialColor: Color) {
	public var color: Color by mutableStateOf(initialColor)
		private set

	internal fun onColorChange(value: Color) {
		color = value
	}

	public companion object {
		internal fun saver(): Saver<DiaryColorState, Any> =
			listSaver(
				save = { listOf(it.color.toArgb()) },
				restore = {
					DiaryColorState(initialColor = Color(it[0]))
				},
			)
	}
}
