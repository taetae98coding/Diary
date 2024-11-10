package io.github.taetae98coding.diary.initializer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import io.github.taetae98coding.diary.app.manager.FCMManager
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication

internal fun initFirebaseMessagingManager(
    koinApplication: KoinApplication,
) {
    val appLifecycleOwner = koinApplication.koin.get<LifecycleOwner>()
    val fcmManager = koinApplication.koin.get<FCMManager>()

    appLifecycleOwner.lifecycleScope.launch {
        fcmManager.attach(appLifecycleOwner)
    }
}
