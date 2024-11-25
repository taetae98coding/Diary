package io.github.taetae98coding.diary.initializer

import io.github.taetae98coding.diary.core.coroutines.AppLifecycleOwner

internal fun initAppLifecycleOwner(): AppLifecycleOwner {
	AppLifecycleOwner.create()
	return AppLifecycleOwner
}
