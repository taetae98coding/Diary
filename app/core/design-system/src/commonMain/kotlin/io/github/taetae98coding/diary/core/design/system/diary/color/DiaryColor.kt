package io.github.taetae98coding.diary.core.design.system.diary.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.core.design.system.color.DiaryColorPickerDialog
import io.github.taetae98coding.diary.library.color.toContrastColor
import io.github.taetae98coding.diary.library.color.toRgbString

@Composable
public fun DiaryColor(
	state: DiaryColorState,
	modifier: Modifier = Modifier,
) {
	val animateColor by animateColorAsState(state.color)
	var isDialogVisible by rememberSaveable { mutableStateOf(false) }

	Card(
		onClick = { isDialogVisible = true },
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = animateColor,
			contentColor = state.color.toContrastColor(),
		),
	) {
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center,
		) {
			Text(text = "#${state.color.toArgb().toRgbString()}")
		}
	}

	if (isDialogVisible) {
		DiaryColorPickerDialog(
			initialColor = state.color,
			onDismissRequest = { isDialogVisible = false },
			onConfirm = { state.onColorChange(it) },
		)
	}
}
