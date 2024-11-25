package io.github.taetae98coding.diary.feature.memo.tag

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.taetae98coding.diary.core.design.system.chip.DiaryFilterChip
import io.github.taetae98coding.diary.core.design.system.icon.TagIcon
import io.github.taetae98coding.diary.library.color.toContrastColor

@Composable
internal fun PrimaryMemoTag(
	uiState: TagUiState,
	modifier: Modifier = Modifier,
) {
	PrimaryMemoTag(
		uiState = uiState,
		onClick = {
			if (uiState.isSelected) {
				uiState.unselect.value()
			} else {
				uiState.select.value()
			}
		},
		modifier = modifier,
	)
}

@Composable
internal fun PrimaryMemoTag(
	uiState: TagUiState,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	MemoTag(
		uiState = uiState,
		onClick = onClick,
		modifier = modifier,
		colors = FilterChipDefaults.filterChipColors(
			selectedContainerColor = Color(uiState.color),
			selectedLabelColor = Color(uiState.color).toContrastColor(),
			selectedLeadingIconColor = Color(uiState.color).toContrastColor(),
		),
	)
}

@Composable
internal fun MemoTag(
	uiState: TagUiState,
	modifier: Modifier = Modifier,
	colors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
) {
	MemoTag(
		uiState = uiState,
		onClick = {
			if (uiState.isSelected) {
				uiState.unselect.value()
			} else {
				uiState.select.value()
			}
		},
		modifier = modifier,
		colors = colors,
	)
}

@Composable
internal fun MemoTag(
	uiState: TagUiState,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	colors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
) {
	DiaryFilterChip(
		selected = uiState.isSelected,
		onClick = onClick,
		label = { Text(text = uiState.title) },
		modifier = modifier,
		leadingIcon = { TagIcon() },
		shape = CircleShape,
		colors = colors,
	)
}
