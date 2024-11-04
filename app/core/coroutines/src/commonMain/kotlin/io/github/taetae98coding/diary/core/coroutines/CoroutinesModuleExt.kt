package io.github.taetae98coding.diary.core.coroutines

import androidx.lifecycle.LifecycleOwner

internal expect fun CoroutinesModule.getAppLifecycleOwner(): LifecycleOwner
