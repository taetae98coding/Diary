package io.github.taetae98coding.diary.feature.account.join.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
internal fun rememberJoinScreenState(): JoinScreenState {
	val coroutineScope = rememberCoroutineScope()

	return rememberSaveable(
		saver = JoinScreenState.saver(coroutineScope),
	) {
		JoinScreenState(coroutineScope)
	}
}
