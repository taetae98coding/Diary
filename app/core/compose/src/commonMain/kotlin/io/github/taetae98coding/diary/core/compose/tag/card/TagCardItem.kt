package io.github.taetae98coding.diary.core.compose.tag.card

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.design.system.chip.DiaryFilterChip
import io.github.taetae98coding.diary.core.design.system.icon.TagIcon

@Composable
public fun TagCardItem(
	uiState: TagCardItemUiState,
	modifier: Modifier = Modifier,
	colors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
) {
	TagCardItem(
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
public fun TagCardItem(
	uiState: TagCardItemUiState,
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
