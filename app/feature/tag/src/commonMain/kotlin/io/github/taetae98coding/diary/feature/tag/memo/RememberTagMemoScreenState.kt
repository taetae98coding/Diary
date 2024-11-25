package io.github.taetae98coding.diary.feature.tag.memo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
internal fun rememberTagMemoScreenState(): TagMemoScreenState {
	val coroutineScope = rememberCoroutineScope()

	return remember {
		TagMemoScreenState(coroutineScope = coroutineScope)
	}
}
