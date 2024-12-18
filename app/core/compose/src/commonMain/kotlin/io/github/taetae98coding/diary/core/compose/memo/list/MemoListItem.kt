package io.github.taetae98coding.diary.core.compose.memo.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.swipe.FinishAndDeleteSwipeBox
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.color.multiplyAlpha

@Composable
public fun MemoListItem(
	uiState: MemoListItemUiState?,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	FinishAndDeleteSwipeBox(
		modifier = modifier,
		onFinish = { uiState?.finish?.value?.invoke() },
		onDelete = { uiState?.delete?.value?.invoke() },
	) {
		Card(onClick = onClick) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(16.dp),
			) {
				MaterialTheme.typography.bodySmall
				Text(
					text = uiState?.title.orEmpty(),
					style = DiaryTheme.typography.titleLarge,
				)
				if (uiState?.dateRange != null) {
					Text(
						text = listOf(uiState.dateRange.start, uiState.dateRange.endInclusive)
							.distinct()
							.joinToString(separator = " ~ "),
						color = LocalContentColor.current.multiplyAlpha(0.5F),
						style = DiaryTheme.typography.labelSmall,
					)
				}
			}
		}
	}
}
