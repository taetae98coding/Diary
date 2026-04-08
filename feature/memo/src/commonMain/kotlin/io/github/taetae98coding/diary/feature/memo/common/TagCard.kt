package io.github.taetae98coding.diary.feature.memo.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.chip.DiaryFilterChip
import io.github.taetae98coding.diary.compose.core.color.ColorCircle
import io.github.taetae98coding.diary.compose.core.icon.ChevronRightIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.compose.ui.wcagAAAContentColor
import kotlin.uuid.Uuid

@Composable
internal fun TagCard(
    uiStateProvider: () -> TagCardUiState,
    onClick: () -> Unit,
    onTag: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Title(onClick = onClick)
        TagFlowRow(
            uiStateProvider = uiStateProvider,
            onTag = onTag,
            modifier = Modifier.fillMaxWidth()
                .height(150.dp),
        )
    }
}

@Composable
private fun Title(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
            .minimumInteractiveComponentSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "태그",
            style = DiaryTheme.typography.titleMedium,
        )
        ChevronRightIcon()
    }
}

@Composable
private fun TagFlowRow(
    uiStateProvider: () -> TagCardUiState,
    onTag: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
    ) {
        when (val uiState = uiStateProvider()) {
            TagCardUiState.Loading -> Unit

            is TagCardUiState.State -> {
                if (uiState.tagList.isEmpty()) {
                    Text(
                        text = "태그가 없습니다",
                        style = DiaryTheme.typography.bodyMedium,
                    )
                } else {
                    uiState.tagList.forEach { tag ->
                        TagChip(
                            uiState = tag,
                            onClick = { onTag(tag.tagId) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TagChip(
    uiState: MemoTagUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = Color(uiState.color)

    DiaryFilterChip(
        selected = false,
        onClick = onClick,
        label = { Text(text = uiState.title) },
        modifier = modifier,
        leadingIcon = {
            ColorCircle(
                colorProvider = { color },
                modifier = Modifier.size(8.dp),
            )
        },
        colors = if (uiState.isPrimary) {
            FilterChipDefaults.filterChipColors(
                containerColor = color,
                labelColor = color.wcagAAAContentColor(),
            )
        } else {
            FilterChipDefaults.filterChipColors()
        },
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        TagCard(
            uiStateProvider = { TagCardUiState.State(emptyList()) },
            onClick = {},
            onTag = {},
        )
    }
}

@ComponentPreview
@Composable
private fun TagPreview() {
    DiaryTheme {
        TagCard(
            uiStateProvider = {
                TagCardUiState.State(
                    tagList = listOf(
                        MemoTagUiState(
                            tagId = Uuid.random(),
                            title = "업무",
                            color = 0xFF4CAF50.toInt(),
                            isPrimary = true,
                            isSelected = false,
                        ),
                        MemoTagUiState(
                            tagId = Uuid.random(),
                            title = "개인",
                            color = 0xFF2196F3.toInt(),
                            isPrimary = false,
                            isSelected = false,
                        ),
                    ),
                )
            },
            onClick = {},
            onTag = {},
        )
    }
}
