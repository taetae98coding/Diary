package io.github.taetae98coding.diary.core.coroutines

import androidx.lifecycle.LifecycleOwner

internal actual fun CoroutinesModule.getAppLifecycleOwner(): LifecycleOwner = AppLifecycleOwner
