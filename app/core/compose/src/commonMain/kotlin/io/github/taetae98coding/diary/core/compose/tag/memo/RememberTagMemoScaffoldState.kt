package io.github.taetae98coding.diary.core.compose.tag.memo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
public fun rememberTagMemoScaffoldState(): TagMemoScaffoldState {
	val coroutineScope = rememberCoroutineScope()

	return remember {
		TagMemoScaffoldState(coroutineScope = coroutineScope)
	}
}
