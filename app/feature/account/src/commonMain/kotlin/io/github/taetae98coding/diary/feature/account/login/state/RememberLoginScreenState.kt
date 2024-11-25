package io.github.taetae98coding.diary.feature.account.login.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
internal fun rememberLoginScreenState(): LoginScreenState {
	val coroutineScope = rememberCoroutineScope()

	return rememberSaveable(saver = LoginScreenState.saver(coroutineScope = coroutineScope)) {
		LoginScreenState(coroutineScope = coroutineScope)
	}
}
