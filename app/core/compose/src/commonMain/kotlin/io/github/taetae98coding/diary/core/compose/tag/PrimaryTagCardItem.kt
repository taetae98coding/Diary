package io.github.taetae98coding.diary.core.compose.tag

import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.taetae98coding.diary.library.color.toContrastColor

@Composable
public fun PrimaryTagCardItem(
    uiState: TagCardItemUiState,
    modifier: Modifier = Modifier,
) {
    PrimaryTagCardItem(
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
public fun PrimaryTagCardItem(
    uiState: TagCardItemUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TagCardItem(
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
