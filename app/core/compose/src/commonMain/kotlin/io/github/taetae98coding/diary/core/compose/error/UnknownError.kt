package io.github.taetae98coding.diary.core.compose.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@Composable
public fun UnknownError(
	onRetry: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center,
	) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			val emoji = remember { Emoji.check.random() }

			Text(
				text = "알 수 없는 에러가 발생했어요 $emoji",
				style = DiaryTheme.typography.headlineMedium,
			)
			TextButton(onClick = onRetry) {
				Text(text = "새로고침")
			}
		}
	}
}
