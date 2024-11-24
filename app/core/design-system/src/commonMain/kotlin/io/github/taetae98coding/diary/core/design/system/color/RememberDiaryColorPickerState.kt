package io.github.taetae98coding.diary.core.design.system.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color

@Composable
public fun rememberDiaryColorPickerState(
	initialColor: Color,
): DiaryColorPickerState =
	rememberSaveable(saver = DiaryColorPickerState.saver()) {
		DiaryColorPickerState(initialColor = initialColor)
	}
