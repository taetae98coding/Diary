package io.github.taetae98coding.diary.core.compose.tag.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
internal fun rememberTagListScaffoldState(
	navigateToTag: (String) -> Unit,
): TagListScaffoldState {
	val coroutineScope = rememberCoroutineScope()

	return remember {
		TagListScaffoldState(
			coroutineScope = coroutineScope,
			navigateToTag = navigateToTag,
		)
	}
}
