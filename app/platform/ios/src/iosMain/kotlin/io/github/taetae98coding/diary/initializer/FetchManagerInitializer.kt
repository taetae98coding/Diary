package io.github.taetae98coding.diary.initializer

import androidx.lifecycle.LifecycleOwner
import io.github.taetae98coding.diary.app.manager.FetchManager
import org.koin.core.KoinApplication

internal fun initFetchManager(
	koinApplication: KoinApplication,
) {
	val appLifecycleOwner = koinApplication.koin.get<LifecycleOwner>()
	val fetchManager = koinApplication.koin.get<FetchManager>()

	fetchManager.attach(appLifecycleOwner)
}
