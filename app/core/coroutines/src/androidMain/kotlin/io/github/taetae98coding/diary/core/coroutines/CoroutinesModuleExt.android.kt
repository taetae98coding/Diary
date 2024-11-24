package io.github.taetae98coding.diary.core.coroutines

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

internal actual fun CoroutinesModule.getAppLifecycleOwner(): LifecycleOwner = ProcessLifecycleOwner.get()
