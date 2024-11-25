package io.github.taetae98coding.diary.initializer

import io.github.taetae98coding.diary.core.coroutines.AppLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun initAppLifecycleOwner(): AppLifecycleOwner {
	withContext(Dispatchers.Main.immediate) {
		AppLifecycleOwner.create()
	}

	return AppLifecycleOwner
}
